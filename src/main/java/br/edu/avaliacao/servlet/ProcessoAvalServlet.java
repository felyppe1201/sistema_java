package br.edu.avaliacao.servlet;

import br.edu.avaliacao.config.EntityManagerUtil;
import br.edu.avaliacao.repositorys.ProcessoAvaliativoRepository;
import br.edu.avaliacao.security.UsuarioSessionDTO;
import br.edu.avaliacao.services.FormulariosAlunoService;
import br.edu.avaliacao.services.FormulariosAlunoService.ProcessoDetalhesDTO;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.persistence.EntityManager;

import java.io.IOException;

@WebServlet("/aluno/processoAval")
public class ProcessoAvalServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            String idParam = request.getParameter("id");
            if (idParam == null) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID do processo é obrigatório.");
                return;
            }
            Long processoId = Long.valueOf(idParam);

            HttpSession session = request.getSession(false);
            if (session == null) {
                response.sendRedirect(request.getContextPath() + "/login");
                return;
            }

            UsuarioSessionDTO usuarioSessao = (UsuarioSessionDTO) session.getAttribute("usuario");
            if (usuarioSessao == null) {
                response.sendRedirect(request.getContextPath() + "/login");
                return;
            }

            Long alunoId = usuarioSessao.getId();

            EntityManager em = EntityManagerUtil.getEntityManager();
            ProcessoAvaliativoRepository repo = new ProcessoAvaliativoRepository(em);

            String nomeProcesso = repo.findNomeById(processoId);
            Long turmaId = repo.findTurmaById(processoId);
            if (nomeProcesso == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Processo não encontrado.");
                return;
            }

            FormulariosAlunoService service = new FormulariosAlunoService();
            ProcessoDetalhesDTO dados = service.obterDetalhesProcesso(processoId, alunoId);

            request.setAttribute("nomeProcesso", nomeProcesso);
            request.setAttribute("naoRespondidos", dados.getNaoRespondidos());
            request.setAttribute("respondidos", dados.getRespondidos());
            request.setAttribute("processoId", processoId);
            request.setAttribute("turmaId", turmaId);

            request.getRequestDispatcher("/WEB-INF/views/aluno/processoAval.jsp")
                   .forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(
                HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                "Erro ao carregar detalhes do processo avaliativo."
            );
        }
    }
}
