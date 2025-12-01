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

        // 1. Pegar id da turma do parâmetro da URL
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

        // 2. Pegar DTO do aluno da sessão
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

            // 3. Buscar turma
            Turma turma = turmaRepo.findById(turmaId);
            if (turma == null || !turma.isAtivo()) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Turma não encontrada");
                return;
            }

            // 4. Buscar disciplina
            var disciplina = disciplinaRepo.findById(turma.getDisciplina().getId());

            // 5. Buscar professor(es) da turma (pegamos o primeiro para exibição)
            List<Usuario> professores = em.createQuery(
                    "SELECT ap.professor FROM AtribuicaoProfessor ap WHERE ap.turma.id = :turmaId AND ap.professor.ativo = true",
                    Usuario.class)
                    .setParameter("turmaId", turmaId)
                    .getResultList();

            Usuario professor = professores.isEmpty() ? null : professores.get(0);

            // 6. Obter lista de processos e formulários para o aluno
            List<ProcessoAlunoDTO> processosAluno = service.listarProcessosAluno(alunoId, turmaId);

            // 7. Enviar atributos para o JSP
            request.setAttribute("turma", turma);
            request.setAttribute("disciplina", disciplina);
            request.setAttribute("professor", professor);
            request.setAttribute("processosAluno", processosAluno);

            // 8. Forward para JSP
            request.getRequestDispatcher("/WEB-INF/views/aluno/turma.jsp").forward(request, response);

        } finally {
            em.close();
        }
    }
}
