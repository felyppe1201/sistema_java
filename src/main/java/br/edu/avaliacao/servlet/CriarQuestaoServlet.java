package br.edu.avaliacao.servlet;

import br.edu.avaliacao.models.Questao;
import br.edu.avaliacao.models.Opcao;
import br.edu.avaliacao.models.Peso;
import br.edu.avaliacao.repositorys.QuestaoRepository;
import br.edu.avaliacao.repositorys.OpcaoRepository;
import br.edu.avaliacao.repositorys.PesoRepository;
import br.edu.avaliacao.config.EntityManagerUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import jakarta.persistence.EntityManager;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

@WebServlet("/lecio/questao")
public class CriarQuestaoServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        Long formId = req.getParameter("form") != null ? Long.parseLong(req.getParameter("form")) : null;

        boolean isNew = req.getParameter("new") != null;
        String qParam = req.getParameter("id_questao");
        Long questaoId = (qParam != null && !qParam.isEmpty()) ? Long.parseLong(qParam) : null;

        Questao questao = null;
        List<Opcao> opcoes = List.of();
        Map<Long, Double> pesosOpcoes = new HashMap<>();

        EntityManager emTemp = EntityManagerUtil.getEntityManager();
        try {
            QuestaoRepository qRepo = new QuestaoRepository(emTemp);
            OpcaoRepository oRepo = new OpcaoRepository(emTemp);
            PesoRepository pRepo = new PesoRepository(emTemp);

            if (questaoId != null) {
                questao = qRepo.findById(questaoId);
                opcoes = oRepo.findByQuestaoId(questaoId);

                // montar lista de ids das opções
                List<Long> opIds = new java.util.ArrayList<>();
                for (Opcao o : opcoes) {
                    opIds.add(o.getId());
                }

                // buscar pesos diretamente ligados às opções
                pesosOpcoes = pRepo.findMapPesosPorOpcoes(opIds);

                if (formId == null && questao != null) {
                    formId = questao.getIdFormulario();
                }
            }
        } finally {
            emTemp.close();
        }

        req.setAttribute("formId", formId);
        req.setAttribute("questao", questao);
        req.setAttribute("opcoes", opcoes);
        req.setAttribute("pesosOpcoes", pesosOpcoes);
        req.setAttribute("isNew", isNew);

        req.getRequestDispatcher("/WEB-INF/views/lecio/questao.jsp").forward(req, resp);
    }
    
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String acao = req.getParameter("acao");

        if ("removerOpcao".equals(acao)) {
            Long opcaoId = Long.parseLong(req.getParameter("id_opcao"));
            Long questaoId = Long.parseLong(req.getParameter("id_questao"));

            EntityManager emTemp = EntityManagerUtil.getEntityManager();
            try {
                OpcaoRepository oRepo = new OpcaoRepository(emTemp);
                emTemp.getTransaction().begin();
                Opcao o = oRepo.findById(opcaoId);
                if (o != null) {
                    oRepo.delete(o);
                }
                emTemp.getTransaction().commit();

                resp.sendRedirect(req.getContextPath() + "/lecio/questao?id_questao=" + questaoId);
                return;
            } catch (Exception e) {
                if (emTemp.getTransaction().isActive()) {
                    emTemp.getTransaction().rollback();
                }
                throw new ServletException("Erro ao remover opção", e);
            } finally {
                emTemp.close();
            }
        }

        Long formId = Long.parseLong(req.getParameter("formId"));
        String texto = req.getParameter("texto");
        String tipo = req.getParameter("tipo");
        boolean obrigatoria = req.getParameter("obrigatoria") != null;
        Double peso = Double.parseDouble(req.getParameter("peso"));
        String questaoIdStr = req.getParameter("id_questao");

        EntityManager emTemp = EntityManagerUtil.getEntityManager();
        try {
            emTemp.getTransaction().begin();

            QuestaoRepository qRepo = new QuestaoRepository(emTemp);
            PesoRepository pRepo = new PesoRepository(emTemp);

            Questao q;

            if (questaoIdStr == null || questaoIdStr.isEmpty()) {
                q = new Questao();
                q.setIdFormulario(formId);
                q.setTexto(texto);
                q.setTipo(tipo);
                q.setObrigatoria(obrigatoria);
                qRepo.save(q);

                Peso p = new Peso();
                p.setIdQuestao(q.getId());
                p.setPeso(peso);
                pRepo.save(p);
            } else {
                Long qid = Long.parseLong(questaoIdStr);
                q = qRepo.findById(qid);

                q.setTexto(texto);
                q.setTipo(tipo);
                q.setObrigatoria(obrigatoria);
                qRepo.update(q);

                Double oldPeso = pRepo.findPesoByQuestaoId(qid);
                if (oldPeso == null) {
                    Peso p = new Peso();
                    p.setIdQuestao(qid);
                    p.setPeso(peso);
                    pRepo.save(p);
                } else {
                    List<Peso> pesos = emTemp.createQuery(
                            "SELECT p FROM Peso p WHERE p.idQuestao = :qid ORDER BY p.id",
                            Peso.class).setParameter("qid", qid).getResultList();

                    if (!pesos.isEmpty()) {
                        Peso p = pesos.get(0);
                        p.setPeso(peso);
                        pRepo.update(p);
                    }
                }
            }

            emTemp.getTransaction().commit();
            resp.sendRedirect(req.getContextPath() + "/lecio/questao?id_questao=" + q.getId());

        } catch (Exception e) {
            if (emTemp.getTransaction().isActive()) {
                emTemp.getTransaction().rollback();
            }
            throw new ServletException("Erro ao salvar questão", e);
        } finally {
            emTemp.close();
        }
    }
}
