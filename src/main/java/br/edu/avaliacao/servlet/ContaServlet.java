package br.edu.avaliacao.servlet;

import br.edu.avaliacao.config.EntityManagerUtil;
import br.edu.avaliacao.repositorys.UsuarioRepository;
import br.edu.avaliacao.repositorys.UsuarioRepository.ContaDTO;
import br.edu.avaliacao.security.UsuarioSessionDTO;

import jakarta.persistence.EntityManager;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet("/conta")
public class ContaServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession(false);

        // 1. Verificação de Sessão
        if (session == null || session.getAttribute("usuario") == null) {
            resp.sendRedirect(req.getContextPath() + "/login.jsp");
            return;
        }

        UsuarioSessionDTO usuario = (UsuarioSessionDTO) session.getAttribute("usuario");

        EntityManager em = EntityManagerUtil.getEntityManager();
        
        try {
            UsuarioRepository usuarioRepo = new UsuarioRepository(em);
            
            Long userIdLong = (long) usuario.getId(); 
            ContaDTO dadosAcademicos = usuarioRepo.buscarDadosAcademicosDoAluno(userIdLong);

            req.setAttribute("nome", usuario.getNome());
            req.setAttribute("email", usuario.getEmail()); 
            req.setAttribute("cursoNome", dadosAcademicos.getCursoNome()); 
            req.setAttribute("periodoAtual", dadosAcademicos.getPeriodoAtual()); 

            String destino = "/WEB-INF/views/conta.jsp";
            req.getRequestDispatcher(destino).forward(req, resp);
            
        } catch (Exception e) {
            System.err.println("Erro ao carregar dados da conta: " + e.getMessage());
            e.printStackTrace();
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Erro interno ao carregar a página da Conta.");
        } finally {
            em.close();
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("usuario") == null) {
            resp.sendRedirect(req.getContextPath() + "/login.jsp");
            return;
        }
        
        UsuarioSessionDTO usuario = (UsuarioSessionDTO) session.getAttribute("usuario");
        Long userIdLong = (long) usuario.getId(); 

        req.setAttribute("nome", usuario.getNome());
        req.setAttribute("email", usuario.getEmail()); 
        
        String action = req.getParameter("action");
        
        if (action == null || action.trim().isEmpty()) {
            req.setAttribute("feedbackMessage", "Ação não especificada.");
            req.setAttribute("feedbackSuccess", false);
            doGet(req, resp); 
            return;
        }

        EntityManager em = EntityManagerUtil.getEntityManager();
        String mensagem = "";
        boolean sucesso = false;
        
        try {
            UsuarioRepository usuarioRepo = new UsuarioRepository(em);

            switch (action) {
                case "updateEmail":
                    String novoEmail = req.getParameter("novoEmail");
                    String senhaAtualEmail = req.getParameter("senhaAtual");

                    if (novoEmail == null || novoEmail.trim().isEmpty() || senhaAtualEmail == null || senhaAtualEmail.trim().isEmpty()) {
                        mensagem = "Preencha o novo email e sua senha atual.";
                        break;
                    }

                    sucesso = usuarioRepo.updateUserEmail(userIdLong, novoEmail, senhaAtualEmail);
                    
                    if (sucesso) {
                        usuario.setEmail(novoEmail);
                        session.setAttribute("usuario", usuario);
                        req.setAttribute("email", novoEmail); 
                        mensagem = "Email alterado com sucesso! O email registrado agora é " + novoEmail + ".";
                    } else {
                        mensagem = "Falha ao alterar email. Verifique sua senha atual ou se o email já está em uso.";
                    }
                    break;
                    
                case "updatePassword":
                    String senhaAntiga = req.getParameter("senhaAntiga");
                    String novaSenha = req.getParameter("novaSenha");
                    String confirmaNovaSenha = req.getParameter("confirmaNovaSenha"); 

                    if (senhaAntiga == null || senhaAntiga.trim().isEmpty() || novaSenha == null || novaSenha.trim().isEmpty() || confirmaNovaSenha == null || confirmaNovaSenha.trim().isEmpty()) {
                        mensagem = "Preencha todos os campos de senha.";
                        break;
                    }

                    if (!novaSenha.equals(confirmaNovaSenha)) {
                        mensagem = "Erro: A nova senha e a confirmação não coincidem.";
                        break;
                    }

                    if (novaSenha.length() < 6) {
                        mensagem = "A nova senha deve ter pelo menos 6 caracteres.";
                        break;
                    }

                    sucesso = usuarioRepo.updateUserPassword(userIdLong, senhaAntiga, novaSenha);
                    
                    if (sucesso) {
                        mensagem = "Senha alterada com sucesso! Você pode usar a nova senha a partir de agora.";
                    } else {
                        mensagem = "Falha ao alterar senha: A senha antiga fornecida está incorreta.";
                    }
                    break;

                default:
                    mensagem = "Ação não reconhecida: " + action;
                    break;
            }

        } catch (Exception e) {
            System.err.println("Erro ao processar requisição POST da Conta: " + e.getMessage());
            e.printStackTrace();
            mensagem = "Erro interno no servidor ao processar a ação.";
            sucesso = false;
        } finally {
            em.close();
        }

        req.setAttribute("feedbackMessage", mensagem);
        req.setAttribute("feedbackSuccess", sucesso);

        doGet(req, resp);
    }
}