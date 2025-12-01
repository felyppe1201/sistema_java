package br.edu.avaliacao.servlet;

import br.edu.avaliacao.config.EntityManagerUtil;
import br.edu.avaliacao.models.*;
import br.edu.avaliacao.repositorys.*;
import jakarta.persistence.EntityManager;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

@WebServlet("/aluno/responder")
public class ResponderAvaliacaoServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        EntityManager em = EntityManagerUtil.getEntityManager();
        try {
            long idForm = Long.parseLong(req.getParameter("idForm"));
            long idTurma = Long.parseLong(req.getParameter("idTurma"));

            FormularioRepository formRepo = new FormularioRepository(em);
            QuestaoRepository qRepo = new QuestaoRepository(em);
            OpcaoRepository opRepo = new OpcaoRepository(em);
            TurmaRepository tRepo = new TurmaRepository(em);

            Formulario form = formRepo.findById(idForm);
            Turma turma = tRepo.findById(idTurma);

            List<Questao> questoes = qRepo.findAll().stream()
                .filter(q -> q.getIdFormulario() == idForm).toList();

            req.setAttribute("formulario", form);
            req.setAttribute("turma", turma);
            req.setAttribute("questoes", questoes);
            req.setAttribute("todasOpcoes", opRepo.findAll()); 

            req.getRequestDispatcher("/WEB-INF/views/aluno/responder-formulario.jsp").forward(req, resp);

        } finally {
            if (em.isOpen()) em.close();
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        
        long idForm = Long.parseLong(req.getParameter("idForm"));
        long idTurma = Long.parseLong(req.getParameter("idTurma"));

        EntityManager em = EntityManagerUtil.getEntityManager();
        
        try {
            em.getTransaction().begin(); 

            FormularioRepository formRepo = new FormularioRepository(em);
            SubmissaoRepository subRepo = new SubmissaoRepository(em);
            RespostaRepository respRepo = new RespostaRepository(em);

            Formulario form = formRepo.findById(idForm);

            Submissao submissao = new Submissao();
            submissao.setIdFormulario(idForm);
            submissao.setIdTurma(idTurma);
            submissao.setDataEnvio(Timestamp.from(Instant.now()));

            if (form.isIdentificado()) {
                submissao.setIdUsuario(usuario.getId());
            } else {

                submissao.setIdUsuario(usuario.getId()); 
            }

            subRepo.save(submissao); 

            QuestaoRepository qRepo = new QuestaoRepository(em);
            List<Questao> questoes = qRepo.findAll().stream()
                .filter(q -> q.getIdFormulario() == idForm).toList();

            for (Questao q : questoes) {
                String valorInput = req.getParameter("resp_" + q.getId());
                
                if (valorInput != null && !valorInput.isEmpty()) {
                    Resposta r = new Resposta();
                    r.setIdSubmissao(submissao.getId());
                    r.setIdQuestao(q.getId());

                    if ("obj".equals(q.getTipo())) {
                        r.setIdOpcao(Long.parseLong(valorInput));
                    } else {
                        r.setTexto(valorInput);
                    }
                    respRepo.save(r);
                }
            }

            em.getTransaction().commit(); 
            
            resp.sendRedirect(req.getContextPath() + "/aluno/avaliacoes?msg=sucesso");

        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            e.printStackTrace();
            resp.sendRedirect(req.getContextPath() + "/aluno/responder?erro=1&idForm="+idForm+"&idTurma="+idTurma);
        } finally {
            if (em.isOpen()) em.close();
        }
    }
}