package br.edu.avaliacao.servlet;

import br.edu.avaliacao.config.EntityManagerUtil;
import br.edu.avaliacao.models.Formulario;
import br.edu.avaliacao.models.Opcao;
import br.edu.avaliacao.models.Questao;
import br.edu.avaliacao.repositorys.FormularioRepository;
import br.edu.avaliacao.repositorys.OpcaoRepository;
import br.edu.avaliacao.repositorys.QuestaoRepository;
import jakarta.persistence.EntityManager;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.List;

@WebServlet("/admin/questoes")
public class QuestaoServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        EntityManager em = EntityManagerUtil.getEntityManager();
        
        try {
            String idFormStr = req.getParameter("idFormulario");
            if (idFormStr == null) {
                resp.sendRedirect(req.getContextPath() + "/admin/formularios");
                return;
            }
            long idFormulario = Long.parseLong(idFormStr);

            FormularioRepository formRepo = new FormularioRepository(em);
            QuestaoRepository questaoRepo = new QuestaoRepository(em);
            OpcaoRepository opcaoRepo = new OpcaoRepository(em);

            Formulario formulario = formRepo.findById(idFormulario);
            
            List<Questao> questoes = questaoRepo.findAll().stream()
                .filter(q -> q.getIdFormulario() == idFormulario)
                .toList();

            req.setAttribute("todasOpcoes", opcaoRepo.findAll()); 
            
            req.setAttribute("formulario", formulario);
            req.setAttribute("questoes", questoes);
            
            req.getRequestDispatcher("/WEB-INF/views/admin/gerenciar-questoes.jsp").forward(req, resp);

        } finally {
            if (em.isOpen()) em.close();
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        EntityManager em = EntityManagerUtil.getEntityManager();
        
        try {
            String acao = req.getParameter("acao");
            long idFormulario = Long.parseLong(req.getParameter("idFormulario"));

            if ("nova_questao".equals(acao)) {
                QuestaoRepository repo = new QuestaoRepository(em);
                Questao q = new Questao();
                q.setIdFormulario(idFormulario);
                q.setTexto(req.getParameter("texto"));
                q.setTipo(req.getParameter("tipo")); 
                q.setObrigatoria(req.getParameter("obrigatoria") != null);
                repo.save(q);
                
            } else if ("nova_opcao".equals(acao)) {
                OpcaoRepository repo = new OpcaoRepository(em);
                Opcao o = new Opcao();
                o.setIdQuestao(Long.parseLong(req.getParameter("idQuestao")));
                o.setTexto(req.getParameter("textoOpcao"));
                repo.save(o);
                
            } else if ("excluir_questao".equals(acao)) {
                 QuestaoRepository repo = new QuestaoRepository(em);
                 repo.delete(Long.parseLong(req.getParameter("idQuestao")));
            }

            resp.sendRedirect(req.getContextPath() + "/admin/questoes?idFormulario=" + idFormulario);

        } catch (Exception e) {
            e.printStackTrace();
            resp.sendError(500, "Erro ao processar questão.");
        } finally {
            if (em.isOpen()) em.close();
        }
    }
}