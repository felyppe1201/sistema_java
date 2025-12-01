package br.edu.avaliacao.servlet;

import br.edu.avaliacao.config.EntityManagerUtil;
import br.edu.avaliacao.models.Formulario;
import br.edu.avaliacao.models.Opcao;
import br.edu.avaliacao.models.Peso;
import br.edu.avaliacao.models.ProcessoAvaliativo;
import br.edu.avaliacao.models.Questao;
import br.edu.avaliacao.models.Turma;
import br.edu.avaliacao.repositorys.FormularioRepository;
import br.edu.avaliacao.repositorys.OpcaoRepository;
import br.edu.avaliacao.repositorys.PesoRepository;
import br.edu.avaliacao.repositorys.ProcessoAvaliativoRepository;
import br.edu.avaliacao.repositorys.QuestaoRepository;
import br.edu.avaliacao.repositorys.TurmaRepository;
import br.edu.avaliacao.security.UsuarioSessionDTO;

import jakarta.persistence.EntityManager;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;
import java.util.regex.Pattern;

@WebServlet("/lecio/criar-forms")
public class CriarFormularioServlet extends HttpServlet {

    private static final Pattern PESO_PATTERN = Pattern.compile("^\\d+(\\.\\d{1,2})?$");

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("usuario") == null) {
            resp.sendRedirect(req.getContextPath() + "/login.jsp");
            return;
        }
        UsuarioSessionDTO usuario = (UsuarioSessionDTO) session.getAttribute("usuario");
        if (!"PROF".equalsIgnoreCase(usuario.getCargo())) {
            resp.sendError(403, "Acesso não autorizado.");
            return;
        }

        String processoIdParam = req.getParameter("id_process");
        String idFormParam = req.getParameter("id_form");

        Long processoId = null; 
        Long idForm = null;

        if (idFormParam != null && !idFormParam.isBlank()) {
            try {
                idForm = Long.parseLong(idFormParam);

                EntityManager emTemp = EntityManagerUtil.getEntityManager();
                try {
                    FormularioRepository fRepo = new FormularioRepository(emTemp);
                    Formulario f = fRepo.findById(idForm);
                    if (f != null) {
                        processoId = f.getIdProcesso(); 
                    }
                } finally {
                    emTemp.close();
                }

            } catch (NumberFormatException ignored) {}
        }

        if (processoId == null) {
            if (processoIdParam == null || processoIdParam.isBlank()) {
                resp.sendError(400, "ID do processo não informado.");
                return;
            }
            try {
                processoId = Long.parseLong(processoIdParam);
            } catch (NumberFormatException e) {
                resp.sendError(400, "ID do processo inválido.");
                return;
            }
        }

        EntityManager em = EntityManagerUtil.getEntityManager();
        try {
            ProcessoAvaliativoRepository procRepo = new ProcessoAvaliativoRepository(em);
            TurmaRepository turmaRepo = new TurmaRepository(em);
            FormularioRepository formRepo = new FormularioRepository(em);
            QuestaoRepository questRepo = new QuestaoRepository(em);
            OpcaoRepository opcRepo = new OpcaoRepository(em);
            PesoRepository pesoRepo = new PesoRepository(em);

            ProcessoAvaliativo processo = procRepo.findById(processoId);
            if (processo == null || !processo.isAtivo()) {
                resp.sendError(404, "Processo não encontrado.");
                return;
            }

            Turma turma = processo.getTurma();
            if (turma == null || !turma.isAtivo()) {
                resp.sendError(404, "Turma do processo não encontrada.");
                return;
            }

            if (!turmaRepo.professorLecionaTurma(usuario.getId(), turma.getId())) {
                resp.sendError(403, "Você não leciona esta turma.");
                return;
            }

            Formulario formulario = null;

            if (idForm != null) {
                formulario = formRepo.findById(idForm);
            }


            List<Map<String, Object>> qView = new ArrayList<>();

            if (formulario != null) {
                List<Questao> qs = questRepo.findByFormularioId(formulario.getId());

                for (Questao q : qs) {
                    Map<String, Object> m = new HashMap<>();
                    m.put("id", q.getId());
                    m.put("texto", q.getTexto());
                    m.put("tipo", q.getTipo());
                    m.put("obrigatoria", q.isObrigatoria());

                    Double pesoQ = pesoRepo.findPesoByQuestaoId(q.getId());
                    m.put("pesoQuestao", pesoQ);
                    qView.add(m);
                }
            }

            req.setAttribute("processo", processo);
            req.setAttribute("formulario", formulario);
            req.setAttribute("questoes", qView);

            req.getRequestDispatcher("/WEB-INF/views/lecio/criar-forms.jsp").forward(req, resp);

        } finally {
            em.close();
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("usuario") == null) {
            resp.sendRedirect(req.getContextPath() + "/login.jsp");
            return;
        }
        UsuarioSessionDTO usuario = (UsuarioSessionDTO) session.getAttribute("usuario");
        if (!"PROF".equalsIgnoreCase(usuario.getCargo())) {
            resp.sendError(403, "Acesso não autorizado.");
            return;
        }

        req.setCharacterEncoding("UTF-8");

        String action = req.getParameter("action");
        if ("deleteQuestao".equals(action)) {

            String idProc = req.getParameter("id_process");
            String idForm = req.getParameter("id_form");
            String idQuest = req.getParameter("id_questao");

            if (idProc == null || idForm == null || idQuest == null) {
                session.setAttribute("msgError", "Dados insuficientes para excluir a questão.");
                resp.sendRedirect(req.getContextPath() + "/lecio/turma");
                return;
            }

            long qid;
            try {
                qid = Long.parseLong(idQuest);
            } catch (NumberFormatException e) {
                session.setAttribute("msgError", "ID da questão inválido.");
                resp.sendRedirect(req.getContextPath() + "/lecio/criar-forms?id_process=" + idProc + "&id_form=" + idForm);
                return;
            }

            EntityManager emDel = EntityManagerUtil.getEntityManager();
            try {
                emDel.getTransaction().begin();
                QuestaoRepository qRepo = new QuestaoRepository(emDel);

                qRepo.delete(qid);

                emDel.getTransaction().commit();
                session.setAttribute("msgSuccess", "Questão excluída com sucesso.");

            } catch (Exception e) {
                emDel.getTransaction().rollback();
                session.setAttribute("msgError", "Erro ao excluir questão: " + e.getMessage());
            } finally {
                emDel.close();
            }

            resp.sendRedirect(req.getContextPath() +
                    "/lecio/criar-forms?id_process=" + idProc + "&id_form=" + idForm);
            return;
        }

        String processoIdParam = req.getParameter("id_process");
        String idFormParam = req.getParameter("id_form");
        String titulo = req.getParameter("titulo");
        String identificadoParam = req.getParameter("identificado");

        if (processoIdParam == null || processoIdParam.isBlank()) {
            session.setAttribute("msgError", "ID do processo não informado.");
            resp.sendRedirect(req.getContextPath() + "/lecio/turma");
            return;
        }

        long processoId;
        try {
            processoId = Long.parseLong(processoIdParam);
        } catch (NumberFormatException e) {
            session.setAttribute("msgError", "ID do processo inválido.");
            resp.sendRedirect(req.getContextPath() + "/lecio/turma");
            return;
        }

        if (titulo == null || titulo.isBlank()) {
            session.setAttribute("msgError", "Título do formulário é obrigatório.");
            resp.sendRedirect(req.getContextPath() + "/lecio/criar-forms?id_process=" + processoId);
            return;
        }

        EntityManager em = EntityManagerUtil.getEntityManager();
        try {
            ProcessoAvaliativoRepository procRepo = new ProcessoAvaliativoRepository(em);
            TurmaRepository turmaRepo = new TurmaRepository(em);
            FormularioRepository formRepo = new FormularioRepository(em);

            ProcessoAvaliativo processo = procRepo.findById(processoId);
            if (processo == null || !processo.isAtivo()) {
                session.setAttribute("msgError", "Processo avaliativo não encontrado.");
                resp.sendRedirect(req.getContextPath() + "/lecio/turma");
                return;
            }
            Turma turma = processo.getTurma();
            if (!turmaRepo.professorLecionaTurma(usuario.getId(), turma.getId())) {
                session.setAttribute("msgError", "Você não leciona esta turma.");
                resp.sendRedirect(req.getContextPath() + "/lecio/turma");
                return;
            }

            Formulario formulario = null;
            if (idFormParam != null && !idFormParam.isBlank()) {
                try {
                    long idf = Long.parseLong(idFormParam);
                    formulario = formRepo.findById(idf);
                } catch (NumberFormatException ignored) { formulario = null; }
            }

            if (formulario == null) {
                // cria novo formulário
                formulario = new Formulario();
                formulario.setIdProcesso(processoId);
                formulario.setTitulo(titulo.trim());
                formulario.setIdentificado(identifiedTrue(identificadoParam));
                formulario.setAtivo(true);
                formulario.setStat(1);
                formRepo.save(formulario);
                session.setAttribute("msgSuccess", "Cabeçalho do formulário criado. Agora você pode adicionar questões.");
            } else {
                formulario.setTitulo(titulo.trim());
                formulario.setIdentificado(identifiedTrue(identificadoParam));
                formRepo.update(formulario);
                session.setAttribute("msgSuccess", "Cabeçalho atualizado.");
            }
            resp.sendRedirect(req.getContextPath() + "/lecio/criar-forms?id_process=" + processoId + "&id_form=" + formulario.getId());
            return;
        } catch (Exception e) {
            e.printStackTrace();
            session.setAttribute("msgError", "Erro ao salvar cabeçalho: " + e.getMessage());
            resp.sendRedirect(req.getContextPath() + "/lecio/criar-forms?id_process=" + processoId);
            return;
        } finally {
            em.close();
        }
    }

    private static boolean identifiedTrue(String param) {
        if (param == null) return false;
        param = param.trim().toLowerCase();
        return param.equals("on") || param.equals("true") || param.equals("1");
    }

    private static boolean isValidPeso(String s) {
        if (s == null) return false;
        s = s.trim().replace(",", ".");
        return PESO_PATTERN.matcher(s).matches();
    }

    private static double round2(double v) {
        return BigDecimal.valueOf(v).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
    }
}
