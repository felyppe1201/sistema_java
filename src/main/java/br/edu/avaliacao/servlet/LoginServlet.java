package br.edu.avaliacao.servlet;

import br.edu.avaliacao.config.EntityManagerUtil;
import br.edu.avaliacao.security.AuthService;
import br.edu.avaliacao.security.UsuarioSessionDTO;
import br.edu.avaliacao.models.Usuario;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import jakarta.persistence.EntityManager;

import java.io.IOException;

@WebServlet("/auth/login")
public class LoginServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.getRequestDispatcher("/WEB-INF/views/login.jsp")
           .forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String email = req.getParameter("email");
        String senha = req.getParameter("senha");

        EntityManager em = EntityManagerUtil.getEntityManager();
        AuthService auth = new AuthService(em);

        Usuario user = auth.autenticar(email, senha);

        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/auth/login?erro=1");
            return;
        }
        
        UsuarioSessionDTO sessionUser = new UsuarioSessionDTO(
            user.getId(),
            user.getNome(),
            user.getEmail(),
            user.getCargo(),
            user.getStat()
        );

        HttpSession session = req.getSession();
        session.setAttribute("usuario", sessionUser);

        resp.sendRedirect(req.getContextPath() + "/dashboard");
    }
}
