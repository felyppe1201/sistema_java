package br.edu.avaliacao.servlet;

import br.edu.avaliacao.config.EntityManagerUtil;
import br.edu.avaliacao.models.Formulario; 
import br.edu.avaliacao.models.ProcessoAvaliativo;
import br.edu.avaliacao.models.Turma;
import br.edu.avaliacao.repositorys.MatriculaRepository;
import br.edu.avaliacao.repositorys.ProcessoAvaliativoRepository;
import br.edu.avaliacao.repositorys.SubmissaoRepository; 
import br.edu.avaliacao.repositorys.TurmaRepository;
import br.edu.avaliacao.security.UsuarioSessionDTO;

import jakarta.persistence.EntityManager;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set; 

@WebServlet("/aluno/turma")
public class TurmaAlunoServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        String dashboardUrl = req.getContextPath() + "/dashboard";

        // 1. Verificação de Sessão
        if (session == null || session.getAttribute("usuario") == null) {
            resp.sendRedirect(req.getContextPath() + "/login.jsp");
            return;
        }

        UsuarioSessionDTO usuario = (UsuarioSessionDTO) session.getAttribute("usuario");

        // 2. Verificação de Cargo (Apenas Aluno)
        if (!"ALU".equalsIgnoreCase(usuario.getCargo())) {
            resp.sendRedirect(dashboardUrl);
            return;
        }

        String idParam = req.getParameter("id");
        if (idParam == null || idParam.isBlank()) {
            resp.sendRedirect(dashboardUrl);
            return;
        }

        EntityManager em = EntityManagerUtil.getEntityManager();

        try {
            long turmaId = Long.parseLong(idParam);
            long alunoId = (long) usuario.getId(); 

            TurmaRepository turmaRepo = new TurmaRepository(em);
            MatriculaRepository matriculaRepo = new MatriculaRepository(em);
            ProcessoAvaliativoRepository processoRepo = new ProcessoAvaliativoRepository(em);
            SubmissaoRepository submissaoRepo = new SubmissaoRepository(em); // Inicializa o novo Repository

            // 3. Buscar a Turma
            Turma turma = turmaRepo.findById(turmaId);
            if (turma == null) {
                resp.sendRedirect(dashboardUrl);
                return;
            }

            // 4. Segurança: Verificar se o aluno está matriculado nesta turma
            List<Object[]> minhasTurmas = matriculaRepo.findTurmasDetalhadasByAlunoId(alunoId);
            boolean isMatriculado = false;

            for (Object[] row : minhasTurmas) {
                Turma t = (Turma) row[0];
                if (t.getId() == turmaId) {
                    isMatriculado = true;
                    break;
                }
            }
            
            if (!isMatriculado) {
                resp.sendError(HttpServletResponse.SC_FORBIDDEN, "Você não está matriculado nesta turma.");
                return;
            }

            // 5. Buscar Processos Avaliativos da Turma
            List<ProcessoAvaliativo> todosProcessos = processoRepo.findByTurmaId(turmaId);
            
            // LÓGICA DE SUBMISSÃO: Obtém os IDs dos Formulários que o aluno já submeteu
            Set<Long> formularioIdsSubmetidos = submissaoRepo.findFormularioIdsSubmetidos(alunoId, turmaId);
            
            List<ProcessoAvaliativo> pendentes = new ArrayList<>();
            List<ProcessoAvaliativo> concluidos = new ArrayList<>();

            // 6. Separar em Pendentes e Concluídos
            for (ProcessoAvaliativo p : todosProcessos) {
                Formulario formularioAssociado = p.getFormulario();

                if (formularioAssociado != null) {
                    Long formularioId = formularioAssociado.getId();
                    
                    // Verifica se o ID do Formulário está no Set de IDs submetidos
                    boolean isConcluido = formularioIdsSubmetidos.contains(formularioId);

                    if (isConcluido) { 
                        // Já submeteu
                        concluidos.add(p); 
                    } else {
                        // Não submeteu: Só adiciona aos pendentes se o processo estiver ativo
                        if (p.isAtivo()) {
                           pendentes.add(p);
                        } else {
                            // Não submeteu e está inativo/fechado
                            concluidos.add(p); 
                        }
                    }
                } else {
                     // Caso não haja formulário associado, trata como concluído/arquivado
                     concluidos.add(p);
                }
            }

            req.setAttribute("turma", turma);
            req.setAttribute("pendentes", pendentes);
            req.setAttribute("concluidos", concluidos);

            req.getRequestDispatcher("/WEB-INF/views/turma-aluno.jsp").forward(req, resp);

        } catch (NumberFormatException e) {
            resp.sendRedirect(dashboardUrl);
        } finally {
            em.close();
        }
    }
}