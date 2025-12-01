package br.edu.avaliacao.servlet;

import br.edu.avaliacao.config.EntityManagerUtil;
import br.edu.avaliacao.models.Disciplina;
import br.edu.avaliacao.repositorys.CursoRepository;
import br.edu.avaliacao.repositorys.DisciplinaRepository;
import jakarta.persistence.EntityManager;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/admin/disciplinas")
public class DisciplinaServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String acao = req.getParameter("acao");
        EntityManager em = EntityManagerUtil.getEntityManager();
        DisciplinaRepository repo = new DisciplinaRepository(em);
        CursoRepository cursoRepo = new CursoRepository(em);

        try {
            req.setAttribute("listaCursos", cursoRepo.findAll());

            if ("editar".equals(acao)) {
                long id = Long.parseLong(req.getParameter("id"));
                req.setAttribute("disciplina", repo.findById(id));
                req.getRequestDispatcher("/WEB-INF/views/admin/form-disciplina.jsp").forward(req, resp);
            } else if ("excluir".equals(acao)) {
                repo.delete(Long.parseLong(req.getParameter("id")));
                resp.sendRedirect(req.getContextPath() + "/admin/disciplinas");
            } else {
                req.setAttribute("disciplinas", repo.findAll());
                req.getRequestDispatcher("/WEB-INF/views/admin/lista-disciplinas.jsp").forward(req, resp);
            }
        } finally {
            if (em.isOpen()) em.close();
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        EntityManager em = EntityManagerUtil.getEntityManager();
        DisciplinaRepository repo = new DisciplinaRepository(em);
        
        try {
            String idStr = req.getParameter("id");
            Disciplina d = new Disciplina();
            if (idStr != null && !idStr.isEmpty()) d = repo.findById(Long.parseLong(idStr));

            d.setNome(req.getParameter("nome"));
            d.setCursoId(Long.parseLong(req.getParameter("idCurso"))); 

            if (d.getId() == 0) repo.save(d);
            else repo.update(d);
            
            resp.sendRedirect(req.getContextPath() + "/admin/disciplinas");
        } finally {
            if (em.isOpen()) em.close();
        }
    }
}