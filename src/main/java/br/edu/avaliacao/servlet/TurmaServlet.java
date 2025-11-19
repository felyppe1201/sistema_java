package br.edu.avaliacao.servlet;

import br.edu.avaliacao.config.EntityManagerUtil;
import br.edu.avaliacao.models.Turma;
import br.edu.avaliacao.repositorys.DisciplinaRepository;
import br.edu.avaliacao.repositorys.TurmaRepository;
import jakarta.persistence.EntityManager;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/admin/turmas")
public class TurmaServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String acao = req.getParameter("acao");
        EntityManager em = EntityManagerUtil.getEntityManager();
        TurmaRepository repo = new TurmaRepository(em);
        DisciplinaRepository discRepo = new DisciplinaRepository(em);

        try {
            // Carrega disciplinas para o dropdown
            req.setAttribute("listaDisciplinas", discRepo.findAll());

            if ("editar".equals(acao)) {
                long id = Long.parseLong(req.getParameter("id"));
                req.setAttribute("turma", repo.findById(id));
                req.getRequestDispatcher("/WEB-INF/views/admin/form-turma.jsp").forward(req, resp);
            } else {
                req.setAttribute("turmas", repo.findAll());
                req.getRequestDispatcher("/WEB-INF/views/admin/lista-turmas.jsp").forward(req, resp);
            }
        } finally {
            if (em.isOpen()) em.close();
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        EntityManager em = EntityManagerUtil.getEntityManager();
        TurmaRepository repo = new TurmaRepository(em);

        try {
            String idStr = req.getParameter("id");
            Turma t = new Turma();
            if (idStr != null && !idStr.isEmpty()) t = repo.findById(Long.parseLong(idStr));

            t.setCodigoTurma(req.getParameter("codigoTurma")); // Ex: T01
            t.setNumeroVagas(Integer.parseInt(req.getParameter("numeroVagas")));
            t.setPeriodo(Integer.parseInt(req.getParameter("periodo"))); // Ex: 20251
            t.setIdDisciplina(Long.parseLong(req.getParameter("idDisciplina")));
            t.setStat(1);

            if (t.getId() == 0) repo.save(t);
            else repo.update(t);

            resp.sendRedirect(req.getContextPath() + "/admin/turmas");
        } finally {
            if (em.isOpen()) em.close();
        }
    }
}