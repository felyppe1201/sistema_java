package br.edu.avaliacao.servlet;

import br.edu.avaliacao.services.RelatorioService;
import br.edu.avaliacao.services.RelatorioService.RelatorioTurmaDTO;
import br.edu.avaliacao.security.UsuarioSessionDTO;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

@WebServlet("/relatorio")
public class RelatorioServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private RelatorioService relatorioService;

    @Override
    public void init() throws ServletException {
        super.init();
        this.relatorioService = new RelatorioService();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null) {
            response.sendRedirect(request.getContextPath() + "/auth/login");
            return;
        }

        UsuarioSessionDTO sessionUser = (UsuarioSessionDTO) session.getAttribute("usuario");

        if (sessionUser == null || !"prof".equalsIgnoreCase(sessionUser.getCargo()) || sessionUser.getStat() != 1) {
            response.sendRedirect(request.getContextPath() + "/auth/login");
            return;
        }

        try {
            List<RelatorioTurmaDTO> relatorios = relatorioService.buscarRelatoriosPorProfessor(sessionUser.getId());

            request.setAttribute("relatorios", relatorios);
            request.setAttribute("nomeProfessor", sessionUser.getNome()); 

            request.getRequestDispatcher("/WEB-INF/views/lecio/relatorio.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("erroRelatorio", "Ocorreu um erro ao carregar os relatórios. Por favor, contate o suporte técnico.");
            request.getRequestDispatcher("/WEB-INF/views/lecio/relatorio.jsp").forward(request, response);
        }
    }
}
