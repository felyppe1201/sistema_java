package br.edu.avaliacao.servlet;

import br.edu.avaliacao.config.EntityManagerFactoryProvider;
import br.edu.avaliacao.config.EntityManagerUtil;
import br.edu.avaliacao.models.*;
import br.edu.avaliacao.repositorys.*;
import br.edu.avaliacao.security.UsuarioSessionDTO;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.util.*;

@WebServlet("/dashboard")
public class DashboardServlet extends HttpServlet {

    private EntityManagerFactory emf;

    @Override
    public void init() {
        emf = EntityManagerFactoryProvider.getEntityManagerFactory();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession(false);

        // Nenhuma sessão → redireciona para login
        if (session == null || session.getAttribute("usuario") == null) {
            resp.sendRedirect(req.getContextPath() + "/login.jsp");
            return;
        }

        // Recupera o usuário logado
        UsuarioSessionDTO usuario = (UsuarioSessionDTO) session.getAttribute("usuario");

        String cargo = usuario.getCargo();

        // ----------- Controle de exibição por cargo -------------
        String destino;

        switch (cargo.toUpperCase()) {
            case "ADM":
                destino = "/WEB-INF/views/dashboard/adminDashboard.jsp";
                carregarDadosAdmin(req);  
                break;

            case "ALU":
                destino = "/WEB-INF/views/dashboard/alunoDashboard.jsp";
                break;

            case "PROF":
                destino = "/WEB-INF/views/dashboard/professorDashboard.jsp";
                break;

            case "COORD":
                destino = "/WEB-INF/views/dashboard/coordDashboard.jsp";
                break;

            default:
                // Cargo inválido → volta para login
                resp.sendRedirect(req.getContextPath() + "/login.jsp?erro=perfil");
                return;
        }

        // Encaminha para a view correta
        req.setAttribute("usuario", usuario);
        req.getRequestDispatcher(destino).forward(req, resp);
    }
     /**
     * Carrega lista de professores e suas turmas
     * para uso no adminDashboard.jsp
     */
    private void carregarDadosAdmin(HttpServletRequest req) {

        EntityManager em = EntityManagerUtil.getEntityManager();

        try {
            AtribuicaoProfessorRepository atribuicaoRepo =
                    new AtribuicaoProfessorRepository(em);
            UsuarioRepository usuarioRepo = new UsuarioRepository(em);

            // 1) Buscar todos os professores (via Repository)
            List<Usuario> professores = usuarioRepo.findProfessoresAtivos();

            // 2) Buscar todas as atribuições
            List<AtribuicaoProfessor> atribuicoes = atribuicaoRepo.findAll();

            // 3) Estrutura final para enviar ao JSP
            List<Map<String, Object>> professoresDTO = new ArrayList<>();

            for (Usuario prof : professores) {

                List<Map<String, Object>> turmasDoProfessor = new ArrayList<>();

                // filtrar turmas deste professor
                for (AtribuicaoProfessor ap : atribuicoes) {
                    if (ap.getProfessor().getId() == prof.getId()) {

                        Turma t = ap.getTurma();

                        Map<String, Object> turmaMap = new HashMap<>();
                        turmaMap.put("id", t.getId());
                        turmaMap.put("disciplinaId", t.getIdDisciplina());
                        turmaMap.put("periodo", t.getPeriodo());
                        turmaMap.put("codigo", t.getCodigoTurma());
                        turmaMap.put("vagas", t.getNumeroVagas());

                        turmasDoProfessor.add(turmaMap);
                    }
                }

                Map<String, Object> profMap = new HashMap<>();
                profMap.put("id", prof.getId());
                profMap.put("nome", prof.getNome());
                profMap.put("quantidadeDeTurmas", turmasDoProfessor.size());
                profMap.put("turmas", turmasDoProfessor);

                professoresDTO.add(profMap);
            }

            req.setAttribute("professores", professoresDTO);

        } finally {
            em.close();
        }
    }

    @Override
    public void destroy() {
        if (emf != null && emf.isOpen()) {
            emf.close();
        }
    }
}
