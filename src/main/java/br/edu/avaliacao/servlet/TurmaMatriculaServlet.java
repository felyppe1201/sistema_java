package br.edu.avaliacao.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Servlet responsável por processar a requisição de acesso à página de Matrícula.
 * * ATENÇÃO: O caminho da VIEW foi ajustado para WEB-INF/views para melhor segurança,
 * garantindo que o JSP só seja acessível via Servlet.
 */
@WebServlet("/turma-matricula")
public class TurmaMatriculaServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    // Caminho corrigido para o arquivo JSP dentro de WEB-INF (prática recomendada)
    private static final String VIEW_PATH = "/WEB-INF/views/turma-matricula.jsp";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // 1. Lógica de Negócio (Busca de dados etc.)
        // ... (Seu código Java para preparar os dados)

        // 2. Encaminhamento (Forward) para o JSP:
        request.getRequestDispatcher(VIEW_PATH).forward(request, response);
    }
}