package br.edu.avaliacao.servlet;

import br.edu.avaliacao.config.EntityManagerUtil;
import br.edu.avaliacao.models.ProcessoAvaliativo;
import br.edu.avaliacao.repositorys.ProcessoAvaliativoRepository;
import jakarta.persistence.EntityManager;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/admin/processos")
public class ProcessoAvaliativoServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String acao = req.getParameter("acao");
        EntityManager em = EntityManagerUtil.getEntityManager();
        ProcessoAvaliativoRepository repo = new ProcessoAvaliativoRepository(em);

        try {
            if ("editar".equals(acao)) {
                long id = Long.parseLong(req.getParameter("id"));
                req.setAttribute("processo", repo.findById(id));
                req.getRequestDispatcher("/WEB-INF/views/admin/form-processo.jsp").forward(req, resp);
            } else {
                req.setAttribute("processos", repo.findAll());
                req.getRequestDispatcher("/WEB-INF/views/admin/lista-processos.jsp").forward(req, resp);
            }
        } finally {
            if (em.isOpen()) em.close();
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        EntityManager em = EntityManagerUtil.getEntityManager();
        ProcessoAvaliativoRepository repo = new ProcessoAvaliativoRepository(em);
        
        try {
            String idStr = req.getParameter("id");
            ProcessoAvaliativo p = new ProcessoAvaliativo();
            if (idStr != null && !idStr.isEmpty()) p = repo.findById(Long.parseLong(idStr));

            p.setNome(req.getParameter("nome"));
            p.setPeriodo(Integer.parseInt(req.getParameter("periodo"))); 
            p.setStat(1);

            if (p.getId() == 0) repo.save(p);
            else repo.update(p);
            
            resp.sendRedirect(req.getContextPath() + "/admin/processos");
        } finally {
            if (em.isOpen()) em.close();
        }
    }
}