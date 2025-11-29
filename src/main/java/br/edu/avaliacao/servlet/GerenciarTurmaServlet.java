package br.edu.avaliacao.servlet;

import br.edu.avaliacao.config.EntityManagerUtil;
import br.edu.avaliacao.models.Turma;
import br.edu.avaliacao.models.ProcessoAvaliativo;
import br.edu.avaliacao.repositorys.TurmaRepository;
import br.edu.avaliacao.repositorys.ProcessoAvaliativoRepository;
import br.edu.avaliacao.security.UsuarioSessionDTO;

import jakarta.persistence.EntityManager;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.util.List;

@WebServlet("/lecio/turma")
public class GerenciarTurmaServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("usuario") == null) {
            resp.sendRedirect(req.getContextPath() + "/login.jsp");
            return;
        }

        UsuarioSessionDTO usuario = (UsuarioSessionDTO) session.getAttribute("usuario");

        if (!"PROF".equalsIgnoreCase(usuario.getCargo())) {
            resp.sendError(403);
            return;
        }

        String idParam = req.getParameter("id");
        if (idParam == null || idParam.isBlank()) {
            resp.sendError(400, "ID não informado.");
            return;
        }

        long turmaId;
        try {
            turmaId = Long.parseLong(idParam);
        } catch (Exception e) {
            resp.sendError(400, "ID inválido.");
            return;
        }

        EntityManager em = EntityManagerUtil.getEntityManager();

        try {
            TurmaRepository turmaRepo = new TurmaRepository(em);
            ProcessoAvaliativoRepository procRepo = new ProcessoAvaliativoRepository(em);

            Turma turma = turmaRepo.findById(turmaId);

            if (turma == null || !turma.isAtivo()) {
                resp.sendError(404, "Turma não encontrada.");
                return;
            }

            if (!turmaRepo.professorLecionaTurma(usuario.getId(), turmaId)) {
                resp.sendError(403, "Você não leciona esta turma.");
                return;
            }

            List<ProcessoAvaliativo> processos = procRepo.findAtivosByTurmaId(turmaId);

            HttpSession s = req.getSession();
            String msgSuccess = (String) s.getAttribute("msgSuccess");
            String msgError = (String) s.getAttribute("msgError");
            if (msgSuccess != null) { req.setAttribute("msgSuccess", msgSuccess); s.removeAttribute("msgSuccess"); }
            if (msgError != null) { req.setAttribute("msgError", msgError); s.removeAttribute("msgError"); }

            req.setAttribute("turma", turma);
            req.setAttribute("processos", processos);

            req.getRequestDispatcher("/WEB-INF/views/lecio/turma-crud.jsp")
               .forward(req, resp);

        } finally {
            em.close();
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("usuario") == null) {
            resp.sendRedirect(req.getContextPath() + "/login.jsp");
            return;
        }

        UsuarioSessionDTO usuario = (UsuarioSessionDTO) session.getAttribute("usuario");

        if (!"PROF".equalsIgnoreCase(usuario.getCargo())) {
            resp.sendError(403);
            return;
        }

        String action = req.getParameter("action");
        String turmaIdParam = req.getParameter("id");

        if (turmaIdParam == null || turmaIdParam.isBlank()) {
            resp.sendError(400);
            return;
        }

        long turmaId;
        try {
            turmaId = Long.parseLong(turmaIdParam);
        } catch (Exception e) {
            resp.sendError(400);
            return;
        }

        EntityManager em = EntityManagerUtil.getEntityManager();

        try {
            TurmaRepository turmaRepo = new TurmaRepository(em);
            ProcessoAvaliativoRepository procRepo = new ProcessoAvaliativoRepository(em);

            Turma turma = turmaRepo.findById(turmaId);

            if (turma == null || !turma.isAtivo()) {
                resp.sendError(404);
                return;
            }

            if (!turmaRepo.professorLecionaTurma(usuario.getId(), turmaId)) {
                resp.sendError(403);
                return;
            }

            if ("create".equalsIgnoreCase(action)) {

                String nome = req.getParameter("nome");
                if (nome == null || nome.isBlank() || nome.length() > 255) {
                    session.setAttribute("msgError", "Nome inválido.");
                    resp.sendRedirect(req.getContextPath() + "/lecio/turma?id=" + turmaId);
                    return;
                }

                ProcessoAvaliativo p = new ProcessoAvaliativo();
                p.setNome(nome.trim());
                p.setPeriodo(turma.getPeriodo());
                p.setTurma(turma);
                p.setAtivo(true);
                p.setStat(1);

                procRepo.save(p);

                session.setAttribute("msgSuccess", "Processo criado.");
                resp.sendRedirect(req.getContextPath() + "/lecio/turma?id=" + turmaId);
                return;

            } else if ("delete".equalsIgnoreCase(action)) {

                String idProcessoParam = req.getParameter("idProcesso");

                if (idProcessoParam == null || idProcessoParam.isBlank()) {
                    session.setAttribute("msgError", "ID do processo não informado.");
                    resp.sendRedirect(req.getContextPath() + "/lecio/turma?id=" + turmaId);
                    return;
                }

                long idProcesso;
                try {
                    idProcesso = Long.parseLong(idProcessoParam);
                } catch (Exception e) {
                    session.setAttribute("msgError", "ID inválido.");
                    resp.sendRedirect(req.getContextPath() + "/lecio/turma?id=" + turmaId);
                    return;
                }

                ProcessoAvaliativo alvo = procRepo.findById(idProcesso);
                if (alvo == null) {
                    session.setAttribute("msgError", "Processo inexistente.");
                    resp.sendRedirect(req.getContextPath() + "/lecio/turma?id=" + turmaId);
                    return;
                }

                if (alvo.getTurma() == null || alvo.getTurma().getId() != turmaId) {
                    session.setAttribute("msgError", "Processo não pertence a esta turma.");
                    resp.sendRedirect(req.getContextPath() + "/lecio/turma?id=" + turmaId);
                    return;
                }

                procRepo.softDelete(idProcesso);

                session.setAttribute("msgSuccess", "Processo excluído.");
                resp.sendRedirect(req.getContextPath() + "/lecio/turma?id=" + turmaId);
                return;
            }

            session.setAttribute("msgError", "Ação inválida.");
            resp.sendRedirect(req.getContextPath() + "/lecio/turma?id=" + turmaId);

        } finally {
            em.close();
        }
    }
}
