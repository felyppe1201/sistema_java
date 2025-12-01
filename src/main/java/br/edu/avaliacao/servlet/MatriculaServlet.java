package br.edu.avaliacao.servlet;

import br.edu.avaliacao.models.Matricula;
import br.edu.avaliacao.repositorys.MatriculaRepository;
import br.edu.avaliacao.security.UsuarioSessionDTO;
import br.edu.avaliacao.services.TurmaDisponivelService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.util.List;

@WebServlet("/aluno/matricula")
public class MatriculaServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("usuario") == null) {
            resp.sendRedirect(req.getContextPath() + "/login.jsp");
            return;
        }

        UsuarioSessionDTO usuario = (UsuarioSessionDTO) session.getAttribute("usuario");
        if (!"ALU".equalsIgnoreCase(usuario.getCargo())) {
            resp.sendError(403, "Acesso não autorizado.");
            return;
        }

        // Chamar service para listar turmas disponíveis
        TurmaDisponivelService service = new TurmaDisponivelService();
        List<TurmaDisponivelService.TurmaDisponivelDTO> turmasDisponiveis = service
                .listarTurmasDisponiveis(usuario.getId());

        req.setAttribute("turmasDisponiveis", turmasDisponiveis);
        req.getRequestDispatcher("/WEB-INF/views/aluno/matricula.jsp").forward(req, resp);
    }
    
    @Override
        protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
            HttpSession session = req.getSession(false);
            if (session == null || session.getAttribute("usuario") == null) {
                resp.sendRedirect(req.getContextPath() + "/login.jsp");
                return;
            }

            UsuarioSessionDTO usuario = (UsuarioSessionDTO) session.getAttribute("usuario");
            if (!"ALU".equalsIgnoreCase(usuario.getCargo())) {
                resp.sendError(403, "Acesso não autorizado.");
                return;
            }

            String turmaIdParam = req.getParameter("turmaId");
            if (turmaIdParam == null || turmaIdParam.isBlank()) {
                session.setAttribute("msgError", "Turma não informada.");
                resp.sendRedirect(req.getContextPath() + "/aluno/matricula");
                return;
            }

            Long turmaId;
            try {
                turmaId = Long.parseLong(turmaIdParam);
            } catch (NumberFormatException e) {
                session.setAttribute("msgError", "Turma inválida.");
                resp.sendRedirect(req.getContextPath() + "/aluno/matricula");
                return;
            }

            var em = br.edu.avaliacao.config.EntityManagerUtil.getEntityManager();
            try {
                em.getTransaction().begin();
                MatriculaRepository repo = new MatriculaRepository(em);

                Matricula matricula = repo.createMatricula(usuario.getId(), turmaId);
                repo.save(matricula);

                em.getTransaction().commit();
                session.setAttribute("msgSuccess", "Matrícula realizada com sucesso!");
            } catch (Exception e) {
                if (em.getTransaction().isActive()) em.getTransaction().rollback();
                e.printStackTrace();
                session.setAttribute("msgError", "Erro ao realizar matrícula: " + e.getMessage());
            } finally {
                em.close();
            }

            resp.sendRedirect(req.getContextPath() + "/dashboard");
        }

}
