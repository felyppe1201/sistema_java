package br.edu.avaliacao.servlet;

import br.edu.avaliacao.config.EntityManagerUtil;
import br.edu.avaliacao.models.Usuario;
import br.edu.avaliacao.repositorys.UsuarioRepository;
import jakarta.persistence.EntityManager;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.List;

@WebServlet("/admin/usuarios")
public class UsuarioServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String acao = req.getParameter("acao");
        EntityManager em = EntityManagerUtil.getEntityManager();
        UsuarioRepository repo = new UsuarioRepository(em);

        try {
            if ("editar".equals(acao)) {
                long id = Long.parseLong(req.getParameter("id"));
                Usuario u = repo.findById(id);
                req.setAttribute("usuario", u);
                req.getRequestDispatcher("/WEB-INF/views/admin/form-usuario.jsp").forward(req, resp);
            } else if ("excluir".equals(acao)) {
                long id = Long.parseLong(req.getParameter("id"));
                repo.delete(id);
                resp.sendRedirect(req.getContextPath() + "/admin/usuarios");
            } else {
                List<Usuario> lista = repo.findAll();
                req.setAttribute("usuarios", lista);
                req.getRequestDispatcher("/WEB-INF/views/admin/lista-usuarios.jsp").forward(req, resp);
            }
        } catch (Exception e) {
            throw new ServletException(e);
        } finally {
            if (em.isOpen()) em.close();
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        EntityManager em = EntityManagerUtil.getEntityManager();
        UsuarioRepository repo = new UsuarioRepository(em);

        try {
            String idStr = req.getParameter("id");
            Usuario u = new Usuario();
            
            // Se for edição, carrega os dados antigos primeiro
            if (idStr != null && !idStr.isEmpty() && !"0".equals(idStr)) {
                u = repo.findById(Long.parseLong(idStr));
            }

            u.setNome(req.getParameter("nome"));
            u.setEmail(req.getParameter("email"));
            u.setCargo(req.getParameter("cargo"));
            u.setAtivo(req.getParameter("ativo") != null);
            u.setStat(1);

            String novaSenha = req.getParameter("senha");
            if (novaSenha != null && !novaSenha.isEmpty()) {
                u.setSenha(novaSenha);
            }

            if (u.getId() == 0) repo.save(u);
            else repo.update(u);

            resp.sendRedirect(req.getContextPath() + "/admin/usuarios");
        } finally {
            if (em.isOpen()) em.close();
        }
    }
}