package br.edu.avaliacao.security;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebFilter("/*")
public class AuthFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        String uri = req.getRequestURI();
        String ctx = req.getContextPath();

        // NOVO: redireciona a rota raiz para o dashboard
        if (uri.equals(ctx + "/") || uri.equals(ctx)) {
            resp.sendRedirect(ctx + "/dashboard");
            return;
        }

        boolean isLoginServlet = uri.equals(ctx + "/auth/login");
        boolean isStatic = uri.startsWith(ctx + "/assets/");
        boolean isLogout = uri.equals(ctx + "/auth/logout");

        HttpSession session = req.getSession(false);
        boolean logado = (session != null && session.getAttribute("usuario") != null);

        // --- BLOQUEIA TENTATIVAS DE ACESSAR JSP DIRETAMENTE ---
        if (uri.endsWith(".jsp")) {
            if (logado) {
                resp.sendRedirect(ctx + "/dashboard");
            } else {
                resp.sendRedirect(ctx + "/auth/login");
            }
            return;
        }

        // --- ROTAS PÚBLICAS (login, logout, assets) ---
        if (isLoginServlet || isStatic || isLogout) {
            chain.doFilter(request, response);
            return;
        }

        // --- BLOQUEIA ROTAS PROTEGIDAS ---
        if (!logado) {
            resp.sendRedirect(ctx + "/auth/login");
            return;
        }

        chain.doFilter(request, response);
    }
}
