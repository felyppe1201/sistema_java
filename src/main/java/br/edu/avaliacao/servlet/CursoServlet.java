package br.edu.avaliacao.servlet;

import br.edu.avaliacao.config.EntityManagerUtil;
import br.edu.avaliacao.models.Curso;
import br.edu.avaliacao.repositorys.CursoRepository;
import jakarta.persistence.EntityManager;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.List;

@WebServlet("/admin/cursos")
public class CursoServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String acao = req.getParameter("acao");
        EntityManager em = EntityManagerUtil.getEntityManager();
        CursoRepository repo = new CursoRepository(em);

        try {
            if ("editar".equals(acao)) {
                long id = Long.parseLong(req.getParameter("id"));
                req.setAttribute("curso", repo.findById(id));
                req.getRequestDispatcher("/WEB-INF/views/admin/form-curso.jsp").forward(req, resp);
            } else if ("excluir".equals(acao)) {
                repo.delete(Long.parseLong(req.getParameter("id")));
                resp.sendRedirect(req.getContextPath() + "/admin/cursos");
            } else {
                List<Curso> lista = repo.findAll();
                req.setAttribute("cursos", lista);
                req.getRequestDispatcher("/WEB-INF/views/admin/lista-cursos.jsp").forward(req, resp);
            }
        } finally {
            if (em.isOpen()) em.close();
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        EntityManager em = EntityManagerUtil.getEntityManager();
        CursoRepository repo = new CursoRepository(em);
        
        try {
            String idStr = req.getParameter("id");
            Curso c = (idStr != null && !idStr.isEmpty() && !"0".equals(idStr)) 
                      ? repo.findById(Long.parseLong(idStr)) : new Curso();
            
            c.setNome(req.getParameter("nome"));

            if (c.getId() == 0) repo.save(c);
            else repo.update(c);
            
            resp.sendRedirect(req.getContextPath() + "/admin/cursos");
        } finally {
            if (em.isOpen()) em.close();
        }
    }
}