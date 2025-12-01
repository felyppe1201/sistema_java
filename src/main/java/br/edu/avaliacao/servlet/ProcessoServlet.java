package br.edu.avaliacao.servlet;

import br.edu.avaliacao.config.EntityManagerUtil;
import br.edu.avaliacao.models.Formulario;
import br.edu.avaliacao.models.ProcessoAvaliativo;
import br.edu.avaliacao.models.Turma;
import br.edu.avaliacao.repositorys.FormularioRepository;
import br.edu.avaliacao.repositorys.ProcessoAvaliativoRepository;
import br.edu.avaliacao.repositorys.TurmaRepository;
import br.edu.avaliacao.security.UsuarioSessionDTO;

import jakarta.persistence.EntityManager;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.util.List;

@WebServlet("/lecio/process")
public class ProcessoServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("usuario") == null) {
            resp.sendRedirect(req.getContextPath() + "/login.jsp");
            return;
        }

        UsuarioSessionDTO usuario = (UsuarioSessionDTO) session.getAttribute("usuario");

        if (!"PROF".equalsIgnoreCase(usuario.getCargo())) {
            resp.sendError(403, "Acesso não autorizado.");
            return;
        }

        String processoIdParam = req.getParameter("id");
        if (processoIdParam == null || processoIdParam.isBlank()) {
            resp.sendError(400, "ID do processo não informado.");
            return;
        }

        long processoId;
        try {
            processoId = Long.parseLong(processoIdParam);
        } catch (NumberFormatException e) {
            resp.sendError(400, "ID do processo inválido.");
            return;
        }

        EntityManager em = EntityManagerUtil.getEntityManager();
        try {
            ProcessoAvaliativoRepository procRepo = new ProcessoAvaliativoRepository(em);
            FormularioRepository formRepo = new FormularioRepository(em);
            TurmaRepository turmaRepo = new TurmaRepository(em);

            ProcessoAvaliativo processo = procRepo.findById(processoId);
            if (processo == null || !processo.isAtivo()) {
                resp.sendError(404, "Processo avaliativo não encontrado.");
                return;
            }

            Turma turma = processo.getTurma();
            if (turma == null || !turma.isAtivo()) {
                resp.sendError(404, "Turma do processo não encontrada.");
                return;
            }

            if (!turmaRepo.professorLecionaTurma(usuario.getId(), turma.getId())) {
                resp.sendError(403, "Você não leciona esta turma.");
                return;
            }

            List<Formulario> formularios = formRepo.findAtivosByProcessoId(processoId);

            HttpSession s = req.getSession();
            String msgSuccess = (String) s.getAttribute("msgSuccess");
            String msgError = (String) s.getAttribute("msgError");
            if (msgSuccess != null) { req.setAttribute("msgSuccess", msgSuccess); s.removeAttribute("msgSuccess"); }
            if (msgError != null) { req.setAttribute("msgError", msgError); s.removeAttribute("msgError"); }

            req.setAttribute("processo", processo);
            req.setAttribute("formularios", formularios);

            req.getRequestDispatcher("/WEB-INF/views/lecio/forms-crud.jsp").forward(req, resp);

        } finally {
            em.close();
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("usuario") == null) {
            resp.sendRedirect(req.getContextPath() + "/login.jsp");
            return;
        }

        UsuarioSessionDTO usuario = (UsuarioSessionDTO) session.getAttribute("usuario");

        if (!"PROF".equalsIgnoreCase(usuario.getCargo())) {
            resp.sendError(403, "Acesso não autorizado.");
            return;
        }

        String action = req.getParameter("action");
        String processoIdParam = req.getParameter("id"); 
        if (processoIdParam == null || processoIdParam.isBlank()) {
            resp.sendError(400, "ID do processo não informado.");
            return;
        }

        long processoId;
        try {
            processoId = Long.parseLong(processoIdParam);
        } catch (NumberFormatException e) {
            resp.sendError(400, "ID do processo inválido.");
            return;
        }

        if (!"delete".equalsIgnoreCase(action)) {
            session.setAttribute("msgError", "Ação inválida.");
            resp.sendRedirect(req.getContextPath() + "/lecio/process?id=" + processoId);
            return;
        }

        String idFormularioParam = req.getParameter("idFormulario");
        if (idFormularioParam == null || idFormularioParam.isBlank()) {
            session.setAttribute("msgError", "ID do formulário não informado.");
            resp.sendRedirect(req.getContextPath() + "/lecio/process?id=" + processoId);
            return;
        }

        long idFormulario;
        try {
            idFormulario = Long.parseLong(idFormularioParam);
        } catch (NumberFormatException e) {
            session.setAttribute("msgError", "ID do formulário inválido.");
            resp.sendRedirect(req.getContextPath() + "/lecio/process?id=" + processoId);
            return;
        }

        EntityManager em = EntityManagerUtil.getEntityManager();
        try {
            ProcessoAvaliativoRepository procRepo = new ProcessoAvaliativoRepository(em);
            FormularioRepository formRepo = new FormularioRepository(em);
            TurmaRepository turmaRepo = new TurmaRepository(em);

            ProcessoAvaliativo processo = procRepo.findById(processoId);
            if (processo == null || !processo.isAtivo()) {
                session.setAttribute("msgError", "Processo não encontrado.");
                resp.sendRedirect(req.getContextPath() + "/lecio/process?id=" + processoId);
                return;
            }

            Turma turma = processo.getTurma();
            if (!turmaRepo.professorLecionaTurma(usuario.getId(), turma.getId())) {
                session.setAttribute("msgError", "Você não leciona esta turma.");
                resp.sendRedirect(req.getContextPath() + "/lecio/process?id=" + processoId);
                return;
            }

            Formulario alvo = formRepo.findById(idFormulario);
            if (alvo == null) {
                session.setAttribute("msgError", "Formulário não encontrado.");
                resp.sendRedirect(req.getContextPath() + "/lecio/process?id=" + processoId);
                return;
            }
            if (alvo.getIdProcesso() != processoId) {
                session.setAttribute("msgError", "Formulário não pertence a este processo.");
                resp.sendRedirect(req.getContextPath() + "/lecio/process?id=" + processoId);
                return;
            }

            formRepo.softDelete(idFormulario);

            session.setAttribute("msgSuccess", "Formulário removido (soft delete).");
            resp.sendRedirect(req.getContextPath() + "/lecio/process?id=" + processoId);
            return;

        } finally {
            em.close();
        }
    }
}
