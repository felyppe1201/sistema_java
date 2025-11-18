package br.edu.avaliacao.servlet;

import br.edu.avaliacao.repository.UsuarioRepository;
import br.edu.avaliacao.utils.ConnectionFactory;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String email = req.getParameter("email");
        String senha = req.getParameter("senha");

        try (Connection conn = ConnectionFactory.getConnection()) {
            UsuarioRepository repo = new UsuarioRepository(conn);

            boolean ok = repo.authenticate(email, senha);

            if (ok) {
                resp.sendRedirect(req.getContextPath() + "/index.jsp");
            } else {
                resp.sendRedirect(req.getContextPath() + "/login.jsp?erro=1");
            }

        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }
}
