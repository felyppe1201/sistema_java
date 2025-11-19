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
                carregarDadosAluno(req, usuario);
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
     * Carrega informações básicas, turmas atuais e turmas disponíveis
     * para uso no alunoDashboard.jsp.
     */
    private void carregarDadosAluno(HttpServletRequest req, UsuarioSessionDTO usuario) {

        req.setAttribute("nome", usuario.getNome());
        req.setAttribute("curso", "Engenharia de Software"); 
        req.setAttribute("periodo", "5º Semestre"); 
        
        List<Map<String, Object>> turmasAtuaisDTO = new ArrayList<>();
        Map<String, Object> t1 = new HashMap<>(); 
        t1.put("codigo", "COMP201"); 
        t1.put("disciplina", "Algoritmos"); 
        t1.put("periodo", "5º Semestre");
        turmasAtuaisDTO.add(t1);
        
        Map<String, Object> t2 = new HashMap<>(); 
        t2.put("codigo", "HUM105"); 
        t2.put("disciplina", "Ética Profissional"); 
        t2.put("periodo", "5º Semestre");
        turmasAtuaisDTO.add(t2);

        List<Map<String, Object>> turmasDisponiveisDTO = new ArrayList<>();
        Map<String, Object> d1 = new HashMap<>(); 
        d1.put("id", 1001); 
        d1.put("codigo", "PROG300"); 
        d1.put("disciplina", "Desenvolvimento Web II"); 
        d1.put("vagas", 15); 
        turmasDisponiveisDTO.add(d1);
        
        Map<String, Object> d2 = new HashMap<>(); 
        d2.put("id", 1002); 
        d2.put("codigo", "CALC101"); 
        d2.put("disciplina", "Cálculo I"); 
        d2.put("vagas", 2); 
        turmasDisponiveisDTO.add(d2);

        req.setAttribute("turmasAtuais", turmasAtuaisDTO);
        req.setAttribute("turmasDisponiveis", turmasDisponiveisDTO);
        
        
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