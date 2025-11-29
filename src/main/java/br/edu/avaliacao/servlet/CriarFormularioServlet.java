package br.edu.avaliacao.servlet;

import br.edu.avaliacao.config.EntityManagerUtil;
import br.edu.avaliacao.models.Formulario;
import br.edu.avaliacao.models.Questao;
import br.edu.avaliacao.models.Opcao;
import br.edu.avaliacao.models.Peso;
import br.edu.avaliacao.models.ProcessoAvaliativo;
import br.edu.avaliacao.models.Turma;
import br.edu.avaliacao.repositorys.FormularioRepository;
import br.edu.avaliacao.repositorys.QuestaoRepository;
import br.edu.avaliacao.repositorys.OpcaoRepository;
import br.edu.avaliacao.repositorys.PesoRepository;
import br.edu.avaliacao.repositorys.ProcessoAvaliativoRepository;
import br.edu.avaliacao.repositorys.TurmaRepository;
import br.edu.avaliacao.security.UsuarioSessionDTO;

import jakarta.persistence.EntityManager;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.math.BigDecimal;
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
        if (processoIdParam == null || processoIdParam.isBlank()) {
            resp.sendError(400, "ID do processo não informado.");
            return;
        }

        long processoId;
        try {
            processoId = Long.parseLong(processoIdParam);
        } catch (NumberFormatException e) {
            resp.sendError(400, "ID do processo inválido.");
            return;
        }

        EntityManager em = EntityManagerUtil.getEntityManager();
        try {
            ProcessoAvaliativoRepository procRepo = new ProcessoAvaliativoRepository(em);
            TurmaRepository turmaRepo = new TurmaRepository(em);

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

            req.setAttribute("processo", processo);
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

        String processoIdParam = req.getParameter("id_process");
        String titulo = req.getParameter("titulo");
        String identificadoParam = req.getParameter("identificado");
        String qCountParam = req.getParameter("q_count");

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

        int qCount = 0;
        try {
            qCount = qCountParam == null ? 0 : Integer.parseInt(qCountParam);
        } catch (NumberFormatException ignored) { qCount = 0; }

        if (qCount <= 0) {
            session.setAttribute("msgError", "É obrigatório criar pelo menos uma questão.");
            resp.sendRedirect(req.getContextPath() + "/lecio/criar-forms?id_process=" + processoId);
            return;
        }

        EntityManager em = EntityManagerUtil.getEntityManager();
        try {
            ProcessoAvaliativoRepository procRepo = new ProcessoAvaliativoRepository(em);
            TurmaRepository turmaRepo = new TurmaRepository(em);

            // valida processo e permissão
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

            // ---------- PRE-VALIDAÇÃO DE PESOS E CAMPOS (evita persistência parcial) ----------
            boolean anyQuestionWithText = false;
            for (int i = 0; i < qCount; i++) {
                String exists = req.getParameter("q_" + i + "_exists");
                if (exists == null) continue;

                String texto = req.getParameter("q_" + i + "_texto");
                if (texto != null && !texto.isBlank()) anyQuestionWithText = true;

                String tipo = req.getParameter("q_" + i + "_tipo");
                if (tipo == null || tipo.isBlank()) tipo = "disc";

                // valida peso da questão (se informado)
                String qPeso = req.getParameter("q_" + i + "_peso");
                if (qPeso != null && !qPeso.isBlank()) {
                    if (!isValidPeso(qPeso)) {
                        session.setAttribute("msgError", "Peso inválido na questão " + (i+1) + ". Use número >= 1 com até 2 casas decimais.");
                        resp.sendRedirect(req.getContextPath() + "/lecio/criar-forms?id_process=" + processoId);
                        return;
                    }
                    double pv = Double.parseDouble(qPeso.replace(",", "."));
                    if (pv < 1.0) {
                        session.setAttribute("msgError", "Peso da questão " + (i+1) + " deve ser >= 1.00.");
                        resp.sendRedirect(req.getContextPath() + "/lecio/criar-forms?id_process=" + processoId);
                        return;
                    }
                }

                if (!"disc".equalsIgnoreCase(tipo)) {
                    int optCount = 0;
                    try { optCount = Integer.parseInt(req.getParameter("q_" + i + "_opt_count")); } catch (Exception ex) { optCount = 0; }
                    if (optCount <= 0) {
                        session.setAttribute("msgError", "Questão " + (i+1) + " precisa ter ao menos uma opção.");
                        resp.sendRedirect(req.getContextPath() + "/lecio/criar-forms?id_process=" + processoId);
                        return;
                    }
                    for (int j = 0; j < optCount; j++) {
                        String optTexto = req.getParameter("q_" + i + "_opt_" + j + "_texto");
                        if (optTexto == null || optTexto.isBlank()) {
                            session.setAttribute("msgError", "Opção vazia na questão " + (i+1) + " — verifique opção " + (j+1) + ".");
                            resp.sendRedirect(req.getContextPath() + "/lecio/criar-forms?id_process=" + processoId);
                            return;
                        }

                        // VF: cada opção deve ter respostavf true/false
                        if ("vf".equalsIgnoreCase(tipo)) {
                            String r = req.getParameter("q_" + i + "_opt_" + j + "_respostavf");
                            if (r == null || !(r.equalsIgnoreCase("true") || r.equalsIgnoreCase("false"))) {
                                session.setAttribute("msgError", "Opção VF sem valor (verdadeiro/falso) na questão " + (i+1) + ".");
                                resp.sendRedirect(req.getContextPath() + "/lecio/criar-forms?id_process=" + processoId);
                                return;
                            }
                        }

                        // valida peso da opção (se informado)
                        String opPeso = req.getParameter("q_" + i + "_opt_" + j + "_peso");
                        if (opPeso != null && !opPeso.isBlank()) {
                            if (!isValidPeso(opPeso)) {
                                session.setAttribute("msgError", "Peso inválido na opção " + (j+1) + " da questão " + (i+1) + ". Use número >= 1 com até 2 decimais.");
                                resp.sendRedirect(req.getContextPath() + "/lecio/criar-forms?id_process=" + processoId);
                                return;
                            }
                            double pv = Double.parseDouble(opPeso.replace(",", "."));
                            if (pv < 1.0) {
                                session.setAttribute("msgError", "Peso da opção " + (j+1) + " da questão " + (i+1) + " deve ser >= 1.00.");
                                resp.sendRedirect(req.getContextPath() + "/lecio/criar-forms?id_process=" + processoId);
                                return;
                            }
                        }
                    }
                }
            } // end pre-validation

            if (!anyQuestionWithText) {
                session.setAttribute("msgError", "Adicione pelo menos uma questão com enunciado.");
                resp.sendRedirect(req.getContextPath() + "/lecio/criar-forms?id_process=" + processoId);
                return;
            }

            // ---------- PERSISTÊNCIA (agora que tudo foi validado) ----------
            Formulario formulario = new Formulario();
            formulario.setIdProcesso(processoId);
            formulario.setTitulo(titulo.trim());
            formulario.setIdentificado(identificadoParam != null && identifiedTrue(identificadoParam));
            formulario.setAtivo(true);
            formulario.setStat(1);

            FormularioRepository formRepo = new FormularioRepository(em);
            QuestaoRepository questRepo = new QuestaoRepository(em);
            OpcaoRepository opcRepo = new OpcaoRepository(em);
            PesoRepository pesoRepo = new PesoRepository(em);

            // salva formulário
            formRepo.save(formulario);

            int createdQuestions = 0;
            for (int i = 0; i < qCount; i++) {
                String exists = req.getParameter("q_" + i + "_exists");
                if (exists == null) continue;

                String tipo = req.getParameter("q_" + i + "_tipo");
                String texto = req.getParameter("q_" + i + "_texto");
                String obrig = req.getParameter("q_" + i + "_obrigatoria");

                if (texto == null || texto.isBlank()) continue;
                if (tipo == null || tipo.isBlank()) tipo = "disc";

                Questao questao = new Questao();
                questao.setIdFormulario(formulario.getId());
                questao.setTexto(texto.trim());
                questao.setTipo(tipo);
                questao.setObrigatoria(obrig != null && (obrig.equalsIgnoreCase("on") || obrig.equalsIgnoreCase("true")));

                questRepo.save(questao);

                // peso da questão (opcional)
                String qPeso = req.getParameter("q_" + i + "_peso");
                if (qPeso != null && !qPeso.isBlank()) {
                    double pesoVal = Double.parseDouble(qPeso.replace(",", "."));
                    Peso pesoQ = new Peso();
                    pesoQ.setIdQuestao(questao.getId());
                    pesoQ.setIdOpcao(null);
                    // arredonda 2 casas
                    pesoQ.setPeso(round2(pesoVal));
                    pesoRepo.save(pesoQ);
                }

                int optCount = 0;
                try { optCount = Integer.parseInt(req.getParameter("q_" + i + "_opt_count")); } catch (Exception ex) { optCount = 0; }

                for (int j = 0; j < optCount; j++) {
                    String optTexto = req.getParameter("q_" + i + "_opt_" + j + "_texto");
                    if (optTexto == null || optTexto.isBlank()) continue;

                    Opcao opcao = new Opcao();
                    opcao.setIdQuestao(questao.getId());
                    opcao.setTexto(optTexto.trim());

                    if ("vf".equalsIgnoreCase(tipo)) {
                        opcao.setVf(true);
                        String r = req.getParameter("q_" + i + "_opt_" + j + "_respostavf");
                        opcao.setRespostavf("true".equalsIgnoreCase(r));
                        opcao.setCorreta(false); // VF ignora o campo correta
                    } else {
                        opcao.setVf(false);
                        opcao.setRespostavf(null);
                        String corr = req.getParameter("q_" + i + "_opt_" + j + "_correta");
                        opcao.setCorreta(corr != null && (corr.equalsIgnoreCase("on") || corr.equalsIgnoreCase("true")));
                    }

                    opcRepo.save(opcao);

                    // peso da opção (opcional)
                    String opPeso = req.getParameter("q_" + i + "_opt_" + j + "_peso");
                    if (opPeso != null && !opPeso.isBlank()) {
                        double pesoVal = Double.parseDouble(opPeso.replace(",", "."));
                        Peso p = new Peso();
                        p.setIdQuestao(questao.getId());
                        p.setIdOpcao(opcao.getId());
                        p.setPeso(round2(pesoVal));
                        pesoRepo.save(p);
                    }
                }

                createdQuestions++;
            }

            if (createdQuestions == 0) {
                session.setAttribute("msgError", "É obrigatório criar pelo menos uma questão com conteúdo.");
                resp.sendRedirect(req.getContextPath() + "/lecio/criar-forms?id_process=" + processoId);
                return;
            }

            session.setAttribute("msgSuccess", "Formulário criado com sucesso.");
            resp.sendRedirect(req.getContextPath() + "/lecio/process?id=" + processoId);
            return;

        } catch (Exception e) {
            e.printStackTrace();
            session.setAttribute("msgError", "Erro ao salvar formulário: " + e.getMessage());
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
