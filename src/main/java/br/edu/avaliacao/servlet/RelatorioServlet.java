package br.edu.avaliacao.servlet; // Pacote alterado para br.edu.avaliacao.servlet

import br.edu.avaliacao.dao.RelatorioDAO;
import br.edu.avaliacao.dao.impl.RelatorioDAOImpl;
import br.edu.avaliacao.dtos.RelatorioTurmaDTO;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

/**
 * Servlet responsável por buscar e exibir os relatórios consolidados para o Professor.
 * Garante que apenas dados AGREGADOS sejam mostrados, cumprindo RF19 (Anonimato).
 */
@WebServlet("/relatorio")
public class RelatorioServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    // A interface DAO para buscar dados agregados no banco.
    private RelatorioDAO relatorioDAO; 

    @Override
    public void init() throws ServletException {
        super.init();
        // Inicializa a implementação DAO (ou injeta, em um framework real).
        this.relatorioDAO = new RelatorioDAOImpl(); 
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {

        // --- 1. Lógica de Autenticação e Autorização ---
        // Simulação da obtenção de dados da sessão.
        Long idUsuarioLogado = (Long) request.getSession().getAttribute("idUsuario");
        String perfilUsuario = (String) request.getSession().getAttribute("perfil");

        // Regra de segurança: O professor precisa estar logado.
        if (idUsuarioLogado == null || idUsuarioLogado <= 0 || !"Professor".equalsIgnoreCase(perfilUsuario)) {
            response.sendRedirect(request.getContextPath() + "/login"); // Redireciona se não for professor ou não estiver logado
            return;
        }

        // --- 2. Busca de Dados Agregados (Garantindo Anonimato) ---
        try {
            // Chama o DAO para buscar dados agregados.
            List<RelatorioTurmaDTO> relatorios = relatorioDAO.buscarRelatoriosConsolidadosPorProfessor(idUsuarioLogado);
            
            // --- 3. Encaminhamento para a View ---
            request.setAttribute("relatorios", relatorios);
            request.setAttribute("nomeProfessor", request.getSession().getAttribute("nomeUsuario"));
            
            // Assume que o JSP está em /views/professor/relatorio.jsp
            request.getRequestDispatcher("/views/professor/relatorio.jsp").forward(request, response);

        } catch (Exception e) {
            // Loga o erro
            System.err.println("Erro crítico ao buscar relatórios consolidados: " + e.getMessage());
            e.printStackTrace();
            
            // Trata o erro de forma amigável
            request.setAttribute("erroRelatorio", "Ocorreu um erro ao carregar os relatórios. Por favor, contate o suporte técnico.");
            request.getRequestDispatcher("/views/professor/relatorio.jsp").forward(request, response);
        }
    }
}