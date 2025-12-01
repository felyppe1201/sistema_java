package br.edu.avaliacao.services;

import br.edu.avaliacao.config.EntityManagerUtil;
import br.edu.avaliacao.models.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RelatorioService {

    public static class RelatorioTurmaDTO {
        private Long turmaId;
        private String codigoTurma;
        private String nomeDisciplina;
        private long totalAlunosMatriculados;
        private long totalRespostasColetadas;
        private double taxaConclusao; // 0..1
        private List<FormularioStatsDTO> formularios;

        public RelatorioTurmaDTO(Long turmaId, String codigoTurma, String nomeDisciplina,
                                 long totalAlunosMatriculados, long totalRespostasColetadas,
                                 double taxaConclusao, List<FormularioStatsDTO> formularios) {
            this.turmaId = turmaId;
            this.codigoTurma = codigoTurma;
            this.nomeDisciplina = nomeDisciplina;
            this.totalAlunosMatriculados = totalAlunosMatriculados;
            this.totalRespostasColetadas = totalRespostasColetadas;
            this.taxaConclusao = taxaConclusao;
            this.formularios = formularios;
        }

        public Long getTurmaId() { return turmaId; }
        public String getCodigoTurma() { return codigoTurma; }
        public String getNomeDisciplina() { return nomeDisciplina; }
        public long getTotalAlunosMatriculados() { return totalAlunosMatriculados; }
        public long getTotalRespostasColetadas() { return totalRespostasColetadas; }
        public double getTaxaConclusao() { return taxaConclusao; }
        public List<FormularioStatsDTO> getFormularios() { return formularios; }
    }

    public static class FormularioStatsDTO {
        private Long idFormulario;
        private String titulo;
        private int totalSubmissions;
        private int totalOptions; 
        private double percentualAcerto; 
        private boolean identificado;
        private List<QuestaoStatDTO> estatisticasQuestoes;

        public FormularioStatsDTO(Long idFormulario, String titulo, int totalSubmissions,
                                  int totalOptions, double percentualAcerto, boolean identificado,
                                  List<QuestaoStatDTO> estatisticasQuestoes) {
            this.idFormulario = idFormulario;
            this.titulo = titulo;
            this.totalSubmissions = totalSubmissions;
            this.totalOptions = totalOptions;
            this.percentualAcerto = percentualAcerto;
            this.identificado = identificado;
            this.estatisticasQuestoes = estatisticasQuestoes;
        }

        public Long getIdFormulario() { return idFormulario; }
        public String getTitulo() { return titulo; }
        public int getTotalSubmissions() { return totalSubmissions; }
        public int getTotalOptions() { return totalOptions; }
        public double getPercentualAcerto() { return percentualAcerto; }
        public boolean isIdentificado() { return identificado; }
        public List<QuestaoStatDTO> getEstatisticasQuestoes() { return estatisticasQuestoes; }
    }

    public static class QuestaoStatDTO {
        private Long idQuestao;
        private String codigoQuestao;
        private String textoQuestao;
        private double scoreTotal; 
        private List<AlternativaStatDTO> alternativas;

        public QuestaoStatDTO(Long idQuestao, String codigoQuestao, String textoQuestao,
                              double scoreTotal, List<AlternativaStatDTO> alternativas) {
            this.idQuestao = idQuestao;
            this.codigoQuestao = codigoQuestao;
            this.textoQuestao = textoQuestao;
            this.scoreTotal = scoreTotal;
            this.alternativas = alternativas;
        }

        public Long getIdQuestao() { return idQuestao; }
        public String getCodigoQuestao() { return codigoQuestao; }
        public String getTextoQuestao() { return textoQuestao; }
        public double getScoreTotal() { return scoreTotal; }
        public List<AlternativaStatDTO> getAlternativas() { return alternativas; }
    }

    public static class AlternativaStatDTO {
        private Long idOpcao;
        private String textoAlternativa;

        private double percentualEscolhida; 
        private double percentualV; 
        private double percentualF; 

        public AlternativaStatDTO(Long idOpcao, String textoAlternativa,
                                  double percentualEscolhida,
                                  double percentualV, double percentualF) {
            this.idOpcao = idOpcao;
            this.textoAlternativa = textoAlternativa;
            this.percentualEscolhida = percentualEscolhida;
            this.percentualV = percentualV;
            this.percentualF = percentualF;
        }

        public Long getIdOpcao() { return idOpcao; }
        public String getTextoAlternativa() { return textoAlternativa; }
        public double getPercentualResposta() { return percentualEscolhida; } // compat.
        public double getPercentualV() { return percentualV; }
        public double getPercentualF() { return percentualF; }
    }

    public List<RelatorioTurmaDTO> buscarRelatoriosPorProfessor(Long professorId) {
        EntityManager em = EntityManagerUtil.getEntityManager();
        try {
            TypedQuery<Turma> qTurmas = em.createQuery(
                    "SELECT ap.turma FROM AtribuicaoProfessor ap WHERE ap.professor.id = :pid AND ap.turma.ativo = true",
                    Turma.class);
            qTurmas.setParameter("pid", professorId);
            List<Turma> turmas = qTurmas.getResultList();

            List<RelatorioTurmaDTO> resultado = new ArrayList<>();

            for (Turma turma : turmas) {
                Long turmaId = turma.getId();

                Long totalMatriculas = 0L;
                try {
                    TypedQuery<Long> qMat = em.createQuery(
                            "SELECT COUNT(m) FROM Matricula m WHERE m.turma.id = :tid",
                            Long.class);
                    qMat.setParameter("tid", turmaId);
                    totalMatriculas = qMat.getSingleResult();
                } catch (Exception ignored) {
                }

                TypedQuery<Formulario> qForms = em.createQuery(
                        "SELECT f FROM Formulario f WHERE f.idProcesso IN (" +
                                " SELECT p.id FROM ProcessoAvaliativo p WHERE p.turma.id = :tid AND p.ativo = true" +
                                ") AND f.ativo = true",
                        Formulario.class);
                qForms.setParameter("tid", turmaId);
                List<Formulario> formularios = qForms.getResultList();

                long totalRespostasColetadas = 0L;
                List<FormularioStatsDTO> formsStats = new ArrayList<>();

                for (Formulario f : formularios) {
                    TypedQuery<Long> qSubsCount = em.createQuery(
                            "SELECT COUNT(s) FROM Submissao s WHERE s.idFormulario = :fid",
                            Long.class);
                    qSubsCount.setParameter("fid", f.getId());
                    Long totalSubsLong = qSubsCount.getSingleResult();
                    int totalSubmissions = (totalSubsLong == null) ? 0 : totalSubsLong.intValue();
                    totalRespostasColetadas += (totalSubsLong == null ? 0L : totalSubsLong);

                    TypedQuery<Submissao> qSubsList = em.createQuery(
                            "SELECT s FROM Submissao s WHERE s.idFormulario = :fid",
                            Submissao.class);
                    qSubsList.setParameter("fid", f.getId());
                    List<Submissao> subsList = qSubsList.getResultList();

                    List<Long> subIds = new ArrayList<>();
                    for (Submissao s : subsList) subIds.add(s.getId());

                    List<Resposta> todasRespostas;
                    if (subIds.isEmpty()) {
                        todasRespostas = List.of();
                    } else {
                        TypedQuery<Resposta> qAllResp = em.createQuery(
                                "SELECT r FROM Resposta r WHERE r.idSubmissao IN :sids",
                                Resposta.class);
                        qAllResp.setParameter("sids", subIds);
                        todasRespostas = qAllResp.getResultList();
                    }

                    Map<Long, List<Resposta>> respostasPorSub = new HashMap<>();
                    Map<Long, List<Resposta>> respostasPorOpcao = new HashMap<>();
                    for (Resposta r : todasRespostas) {
                        respostasPorSub.computeIfAbsent(r.getIdSubmissao(), k -> new ArrayList<>()).add(r);
                        if (r.getIdOpcao() != null)
                            respostasPorOpcao.computeIfAbsent(r.getIdOpcao(), k -> new ArrayList<>()).add(r);
                    }

                    TypedQuery<Questao> qQuests = em.createQuery(
                            "SELECT q FROM Questao q WHERE q.idFormulario = :fid AND q.tipo <> 'disc'",
                            Questao.class);
                    qQuests.setParameter("fid", f.getId());
                    List<Questao> questoes = qQuests.getResultList();

                    int totalOptionsInForm = 0;
                    int acumuladoMarcasCorretasFormulario = 0;

                    List<QuestaoStatDTO> questaoStats = new ArrayList<>();

                    boolean formularioIdentificado = Boolean.TRUE.equals(f.isIdentificado()) || (f.isIdentificado() == true);

                    for (Questao q : questoes) {
                        TypedQuery<Opcao> qOps = em.createQuery(
                                "SELECT o FROM Opcao o WHERE o.idQuestao = :qid",
                                Opcao.class);
                        qOps.setParameter("qid", q.getId());
                        List<Opcao> opcoes = qOps.getResultList();

                        int totalOptionsThisQuest = opcoes.size();
                        totalOptionsInForm += totalOptionsThisQuest;

                        long acumuladoMarcasCorretasQuest = 0L; 
                        List<AlternativaStatDTO> altStats = new ArrayList<>();

                        for (Opcao op : opcoes) {
                            List<Resposta> respForOption = respostasPorOpcao.getOrDefault(op.getId(), List.of());
                            long countChosen = respForOption.size(); 
                            long countTrue = 0L;
                            long countFalse = 0L;
                            long marcasCorretasEstaOpcao = 0L;

                            if (!respForOption.isEmpty()) {
                                for (Resposta rr : respForOption) {
                                    if (rr.getRespostaVf() != null) {
                                        if (rr.getRespostaVf()) countTrue++;
                                        else countFalse++;
                                        Boolean opVf = op.getVf();
                                        if (opVf != null && rr.getRespostaVf().equals(opVf)) {
                                            marcasCorretasEstaOpcao++;
                                        }
                                    } else {

                                    }
                                }
                            }

                            if ("vf".equalsIgnoreCase(q.getTipo())) {
                                double percentV = (totalSubmissions == 0) ? 0.0 : (double) countTrue / (double) totalSubmissions;
                                double percentF = (totalSubmissions == 0) ? 0.0 : (double) countFalse / (double) totalSubmissions;

                                altStats.add(new AlternativaStatDTO(op.getId(), op.getTexto(), 0.0, percentV, percentF));
                                acumuladoMarcasCorretasQuest += marcasCorretasEstaOpcao;
                            } else {
                                double percentEscolhida = (totalSubmissions == 0) ? 0.0 : (double) countChosen / (double) totalSubmissions;

                                boolean opcaoCorreta = Boolean.TRUE.equals(op.getCorreta());
                                if (opcaoCorreta) {
                                    marcasCorretasEstaOpcao += countChosen;
                                } else {
                                    marcasCorretasEstaOpcao += (long) (totalSubmissions - countChosen);
                                }

                                altStats.add(new AlternativaStatDTO(op.getId(), op.getTexto(), percentEscolhida, 0.0, 0.0));
                                acumuladoMarcasCorretasQuest += marcasCorretasEstaOpcao;
                            }
                        } 

                        double scoreTotal = 0.0;
                        if (totalOptionsThisQuest > 0 && totalSubmissions > 0) {
                            double denom = (double) totalOptionsThisQuest * (double) totalSubmissions;
                            scoreTotal = ((double) acumuladoMarcasCorretasQuest / denom) * 100.0;
                        }

                        questaoStats.add(new QuestaoStatDTO(q.getId(), String.valueOf(q.getId()), q.getTexto(), scoreTotal, altStats));
                    } 

                    long acumuladoMarcasCorretasRecalc = 0L;
                    for (Questao q : questoes) {
                        TypedQuery<Opcao> qOps2 = em.createQuery(
                                "SELECT o FROM Opcao o WHERE o.idQuestao = :qid",
                                Opcao.class);
                        qOps2.setParameter("qid", q.getId());
                        List<Opcao> opcoes2 = qOps2.getResultList();

                        for (Opcao op : opcoes2) {
                            List<Resposta> respForOption = respostasPorOpcao.getOrDefault(op.getId(), List.of());
                            long countChosen = respForOption.size();

                            if ("vf".equalsIgnoreCase(q.getTipo())) {
                                long marcasCorretasEstaOpcao = 0L;
                                for (Resposta rr : respForOption) {
                                    Boolean opcVf = op.getVf();
                                    if (rr.getRespostaVf() != null && opcVf != null && rr.getRespostaVf().equals(opcVf)) {
                                        marcasCorretasEstaOpcao++;
                                    }
                                }
                                acumuladoMarcasCorretasRecalc += marcasCorretasEstaOpcao;
                            } else {
                                // objetiva
                                boolean opcaoCorreta = Boolean.TRUE.equals(op.getCorreta());
                                if (opcaoCorreta) {
                                    acumuladoMarcasCorretasRecalc += countChosen;
                                } else {
                                    acumuladoMarcasCorretasRecalc += (long) (totalSubmissions - countChosen);
                                }
                            }
                        }
                    }

                    double percentualAcerto = 0.0;
                    if (totalOptionsInForm > 0 && totalSubmissions > 0) {
                        double denom = (double) totalOptionsInForm * (double) totalSubmissions;
                        percentualAcerto = ((double) acumuladoMarcasCorretasRecalc / denom) * 100.0;
                    }

                    boolean identificado = Boolean.TRUE.equals(f.isIdentificado());

                    List<QuestaoStatDTO> questaoStatsToReturn = identificado ? questaoStats : new ArrayList<>();

                    FormularioStatsDTO formStats = new FormularioStatsDTO(
                            f.getId(),
                            f.getTitulo(),
                            totalSubmissions,
                            totalOptionsInForm,
                            identificado ? percentualAcerto : 0.0,
                            identificado,
                            questaoStatsToReturn
                    );

                    formsStats.add(formStats);
                } 

                double taxaConclusao = 0.0;
                int totalFormularios = formularios.size();
                if (totalMatriculas > 0 && totalFormularios > 0) {
                    double denom = (double) (totalMatriculas * totalFormularios);
                    taxaConclusao = ((double) totalRespostasColetadas / denom);
                    if (taxaConclusao > 1.0) taxaConclusao = 1.0;
                }

                String nomeDisciplina = null;
                try {
                    nomeDisciplina = turma.getDisciplina() != null ? turma.getDisciplina().getNome() : null;
                } catch (Exception ignored) {}

                RelatorioTurmaDTO turmaDto = new RelatorioTurmaDTO(
                        turmaId,
                        turma.getCodigoTurma(),
                        nomeDisciplina,
                        (totalMatriculas == null ? 0L : totalMatriculas),
                        totalRespostasColetadas,
                        taxaConclusao,
                        formsStats
                );
                resultado.add(turmaDto);
            } 

            return resultado;
        } finally {
            em.close();
        }
    }
}
