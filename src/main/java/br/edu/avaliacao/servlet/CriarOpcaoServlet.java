package br.edu.avaliacao.servlet;

import br.edu.avaliacao.models.Opcao;
import br.edu.avaliacao.models.Questao;
import br.edu.avaliacao.models.Peso;
import br.edu.avaliacao.repositorys.OpcaoRepository;
import br.edu.avaliacao.repositorys.QuestaoRepository;
import br.edu.avaliacao.repositorys.PesoRepository;
import br.edu.avaliacao.config.EntityManagerUtil;

import jakarta.persistence.EntityManager;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.util.List;

@WebServlet("/lecio/opcao")
public class CriarOpcaoServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String questaoParam = req.getParameter("questao");
        String opcaoParam = req.getParameter("opcao");

        if (questaoParam == null || questaoParam.isBlank()) {
            resp.sendError(400, "ID da questão (questao) é obrigatório.");
            return;
        }

        Long questaoId;
        try {
            questaoId = Long.parseLong(questaoParam);
        } catch (NumberFormatException e) {
            resp.sendError(400, "ID da questão inválido.");
            return;
        }

        Long opcaoId = null;
        if (opcaoParam != null && !opcaoParam.isBlank()) {
            try {
                opcaoId = Long.parseLong(opcaoParam);
            } catch (NumberFormatException ignored) {}
        }

        EntityManager em = EntityManagerUtil.getEntityManager();
        try {
            QuestaoRepository qRepo = new QuestaoRepository(em);
            OpcaoRepository oRepo = new OpcaoRepository(em);
            PesoRepository pRepo = new PesoRepository(em);

            Questao questao = qRepo.findById(questaoId);
            if (questao == null) {
                resp.sendError(404, "Questão não encontrada.");
                return;
            }

            if ("disc".equalsIgnoreCase(questao.getTipo())) {
                resp.sendError(400, "Questão dissertativa não aceita opções.");
                return;
            }

            Opcao opcao = null;
            Double pesoValor = null;

            if (opcaoId != null) {
                opcao = oRepo.findById(opcaoId);
                if (opcao == null) {
                    resp.sendError(404, "Opção não encontrada.");
                    return;
                }
                pesoValor = pRepo.findPesoByOpcaoId(opcaoId);
            }

            req.setAttribute("questao", questao);
            req.setAttribute("opcao", opcao);
            req.setAttribute("pesoValor", pesoValor);
            req.setAttribute("isNew", opcao == null);

            req.getRequestDispatcher("/WEB-INF/views/lecio/opcao.jsp").forward(req, resp);

        } finally {
            em.close();
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String questaoParam = req.getParameter("questao");
        String opcaoParam = req.getParameter("opcao");
        String texto = req.getParameter("texto");
        String pesoStr = req.getParameter("peso");

        if (questaoParam == null || questaoParam.isBlank()) {
            resp.sendError(400, "ID da questão é obrigatório.");
            return;
        }

        Long questaoId;
        try {
            questaoId = Long.parseLong(questaoParam);
        } catch (NumberFormatException e) {
            resp.sendError(400, "ID da questão inválido.");
            return;
        }

        if (pesoStr == null || pesoStr.isBlank()) {
            req.setAttribute("msgError", "O peso é obrigatório.");
            resp.sendRedirect(req.getContextPath() + "/lecio/opcao?questao=" + questaoId);
            return;
        }

        double pesoValor;
        try {
            pesoValor = Double.parseDouble(pesoStr);
        } catch (Exception e) {
            req.setAttribute("msgError", "Peso inválido.");
            resp.sendRedirect(req.getContextPath() + "/lecio/opcao?questao=" + questaoId);
            return;
        }

        if (pesoValor < 1) {
            req.setAttribute("msgError", "O peso da opção não pode ser menor que 1.");
            resp.sendRedirect(req.getContextPath() + "/lecio/opcao?questao=" + questaoId);
            return;
        }

        EntityManager em = EntityManagerUtil.getEntityManager();
        try {
            QuestaoRepository qRepo = new QuestaoRepository(em);
            OpcaoRepository oRepo = new OpcaoRepository(em);
            PesoRepository pRepo = new PesoRepository(em);

            Questao questao = qRepo.findById(questaoId);
            if (questao == null) {
                resp.sendError(404, "Questão não encontrada.");
                return;
            }

            if ("disc".equalsIgnoreCase(questao.getTipo())) {
                resp.sendError(400, "Não é permitido adicionar opções à questão dissertativa.");
                return;
            }

            em.getTransaction().begin();

            Opcao opcao;
            boolean creating = (opcaoParam == null || opcaoParam.isBlank());
            Long opcaoId = null;

            if (creating) {
                opcao = new Opcao();
                opcao.setIdQuestao(questaoId);
            } else {
                try {
                    opcaoId = Long.parseLong(opcaoParam);
                } catch (NumberFormatException e) {
                    em.getTransaction().rollback();
                    resp.sendError(400, "ID de opção inválido.");
                    return;
                }

                opcao = oRepo.findById(opcaoId);
                if (opcao == null) {
                    em.getTransaction().rollback();
                    resp.sendError(404, "Opção não encontrada.");
                    return;
                }
            }

            opcao.setTexto(texto != null ? texto.trim() : "");

            String tipoQuestao = questao.getTipo();

            if ("vf".equalsIgnoreCase(tipoQuestao)) {
                String res = req.getParameter("respostavf");
                boolean r = "true".equalsIgnoreCase(res);

                opcao.setVf(true);
                opcao.setRespostavf(r);
                opcao.setCorreta(false);

            } else if ("obj".equalsIgnoreCase(tipoQuestao)) {
                boolean correta = req.getParameter("correta") != null;

                opcao.setVf(false);
                opcao.setRespostavf(null);
                opcao.setCorreta(correta);
            }

            if (creating) oRepo.save(opcao);
            else oRepo.update(opcao);

            Double oldPeso = pRepo.findPesoByOpcaoId(opcao.getId());

            if (oldPeso == null) {
                Peso p = new Peso();
                p.setIdOpcao(opcao.getId());
                p.setPeso(pesoValor);
                pRepo.save(p);
            } else {
                List<Peso> pesos = em.createQuery(
                        "SELECT p FROM Peso p WHERE p.idOpcao = :oid ORDER BY p.id",
                        Peso.class
                ).setParameter("oid", opcao.getId()).getResultList();

                if (!pesos.isEmpty()) {
                    Peso p = pesos.get(0);
                    p.setPeso(pesoValor);
                    pRepo.update(p);
                }
            }

            em.getTransaction().commit();

            resp.sendRedirect(req.getContextPath() + "/lecio/questao?id_questao=" + questaoId);

        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw new ServletException("Erro ao salvar opção", e);
        } finally {
            em.close();
        }
    }
}
