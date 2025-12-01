package br.edu.avaliacao.servlet;

import br.edu.avaliacao.config.EntityManagerUtil;
import br.edu.avaliacao.models.Turma;
import br.edu.avaliacao.models.Usuario;
import br.edu.avaliacao.repositorys.TurmaRepository;
import br.edu.avaliacao.repositorys.DisciplinaRepository;
import br.edu.avaliacao.services.FormulariosAlunoService;
import br.edu.avaliacao.services.FormulariosAlunoService.ProcessoAlunoDTO;
import br.edu.avaliacao.security.UsuarioSessionDTO;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.persistence.EntityManager;

import java.io.IOException;
import java.util.List;

@WebServlet("/turma")
public class TurmaAlunoServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String turmaIdStr = request.getParameter("id");
        if (turmaIdStr == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID da turma não informado");
            return;
        }
        Long turmaId;
        try {
            turmaId = Long.parseLong(turmaIdStr);
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID da turma inválido");
            return;
        }

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("usuario") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        UsuarioSessionDTO usuarioSessao = (UsuarioSessionDTO) session.getAttribute("usuario");
        Long alunoId = usuarioSessao.getId();

        EntityManager em = EntityManagerUtil.getEntityManager();
        try {
            TurmaRepository turmaRepo = new TurmaRepository(em);
            DisciplinaRepository disciplinaRepo = new DisciplinaRepository(em);
            FormulariosAlunoService service = new FormulariosAlunoService();

            Turma turma = turmaRepo.findById(turmaId);
            if (turma == null || !turma.isAtivo()) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Turma não encontrada");
                return;
            }

            var disciplina = disciplinaRepo.findById(turma.getDisciplina().getId());

            List<Usuario> professores = em.createQuery(
                    "SELECT ap.professor FROM AtribuicaoProfessor ap WHERE ap.turma.id = :turmaId AND ap.professor.ativo = true",
                    Usuario.class)
                    .setParameter("turmaId", turmaId)
                    .getResultList();

            Usuario professor = professores.isEmpty() ? null : professores.get(0);

            List<ProcessoAlunoDTO> processosAluno = service.listarProcessosAluno(alunoId, turmaId);

            request.setAttribute("turma", turma);
            request.setAttribute("disciplina", disciplina);
            request.setAttribute("professor", professor);
            request.setAttribute("processosAluno", processosAluno);

            request.getRequestDispatcher("/WEB-INF/views/aluno/turma.jsp").forward(request, response);

        } finally {
            em.close();
        }
    }
}
