package br.edu.avaliacao.services;

import br.edu.avaliacao.config.EntityManagerUtil;
import br.edu.avaliacao.models.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import java.util.ArrayList;
import java.util.List;

public class FormulariosAlunoService {

    public static class FormNaoRespondidoDTO {
        private Long idFormulario;
        private String titulo;
        private boolean identificado;

        public FormNaoRespondidoDTO(Long idFormulario, String titulo, boolean identificado) {
            this.idFormulario = idFormulario;
            this.titulo = titulo;
            this.identificado = identificado;
        }

        public Long getIdFormulario() { return idFormulario; }
        public String getTitulo() { return titulo; }
        public boolean isIdentificado() { return identificado; }
    }

    public static class FormRespondidoDTO {
        private Long idFormulario;
        private String titulo;
        private boolean identificado;
        private double percentualAcerto;

        public FormRespondidoDTO(Long idFormulario, String titulo, boolean identificado, double percentualAcerto) {
            this.idFormulario = idFormulario;
            this.titulo = titulo;
            this.identificado = identificado;
            this.percentualAcerto = percentualAcerto;
        }

        public Long getIdFormulario() { return idFormulario; }
        public String getTitulo() { return titulo; }
        public boolean isIdentificado() { return identificado; }
        public double getPercentualAcerto() { return percentualAcerto; }
    }

    public static class ProcessoDetalhesDTO {
        private List<FormNaoRespondidoDTO> naoRespondidos;
        private List<FormRespondidoDTO> respondidos;

        public ProcessoDetalhesDTO(List<FormNaoRespondidoDTO> naoRespondidos,
                                List<FormRespondidoDTO> respondidos) {
            this.naoRespondidos = naoRespondidos;
            this.respondidos = respondidos;
        }

        public List<FormNaoRespondidoDTO> getNaoRespondidos() { return naoRespondidos; }
        public List<FormRespondidoDTO> getRespondidos() { return respondidos; }
    }


    public static class ProcessoAlunoDTO {
        private Long idProcesso;
        private String nomeProcesso;
        private int totalFormularios;
        private int respondidos;

        public ProcessoAlunoDTO(Long idProcesso, String nomeProcesso, int totalFormularios, int respondidos) {
            this.idProcesso = idProcesso;
            this.nomeProcesso = nomeProcesso;
            this.totalFormularios = totalFormularios;
            this.respondidos = respondidos;
        }

        public Long getIdProcesso() { return idProcesso; }
        public String getNomeProcesso() { return nomeProcesso; }
        public int getTotalFormularios() { return totalFormularios; }
        public int getRespondidos() { return respondidos; }
    }

    public List<ProcessoAlunoDTO> listarProcessosAluno(Long alunoId, Long turmaId) {

        EntityManager em = EntityManagerUtil.getEntityManager();
        try {
            TypedQuery<ProcessoAvaliativo> queryProcessos = em.createQuery(
                    "SELECT p FROM ProcessoAvaliativo p WHERE p.turma.id = :turmaId AND p.ativo = true",
                    ProcessoAvaliativo.class);
            queryProcessos.setParameter("turmaId", turmaId);
            List<ProcessoAvaliativo> processos = queryProcessos.getResultList();

            List<ProcessoAlunoDTO> resultado = new ArrayList<>();

            for (ProcessoAvaliativo p : processos) {
                TypedQuery<Formulario> queryFormularios = em.createQuery(
                        "SELECT f FROM Formulario f WHERE f.idProcesso = :processoId AND f.ativo = true",
                        Formulario.class);
                queryFormularios.setParameter("processoId", p.getId());
                List<Formulario> formularios = queryFormularios.getResultList();

                int totalFormularios = formularios.size();
                int respondidos = 0;

                for (Formulario f : formularios) {
                    TypedQuery<Long> querySubmissao = em.createQuery(
                            "SELECT COUNT(s) FROM Submissao s WHERE s.idFormulario = :formularioId AND s.idUsuario = :alunoId",
                            Long.class);
                    querySubmissao.setParameter("formularioId", f.getId());
                    querySubmissao.setParameter("alunoId", alunoId);
                    Long count = querySubmissao.getSingleResult();
                    if (count > 0)
                        respondidos++;
                }

                resultado.add(new ProcessoAlunoDTO(p.getId(), p.getNome(), totalFormularios, respondidos));
            }

            return resultado;

        } finally {
            em.close();
        }
    }

    public ProcessoDetalhesDTO obterDetalhesProcesso(Long processoId, Long alunoId) {
        EntityManager em = EntityManagerUtil.getEntityManager();

        try {
            TypedQuery<Formulario> qForms = em.createQuery(
                "SELECT f FROM Formulario f WHERE f.idProcesso = :pid AND f.ativo = true",
                Formulario.class
            );
            qForms.setParameter("pid", processoId);
            List<Formulario> formularios = qForms.getResultList();

            List<FormNaoRespondidoDTO> naoRespondidos = new ArrayList<>();
            List<FormRespondidoDTO> respondidos = new ArrayList<>();

            for (Formulario f : formularios) {

                TypedQuery<Submissao> qSub = em.createQuery(
                    "SELECT s FROM Submissao s WHERE s.idFormulario = :fid AND s.idUsuario = :uid",
                    Submissao.class
                );
                qSub.setParameter("fid", f.getId());
                qSub.setParameter("uid", alunoId);

                List<Submissao> subs = qSub.getResultList();
                boolean respondeu = !subs.isEmpty();
                boolean identificado = f.isIdentificado();

                if (!respondeu) {
                    naoRespondidos.add(
                        new FormNaoRespondidoDTO(
                            f.getId(),
                            f.getTitulo(),
                            identificado
                        )
                    );
                    continue;
                }

                double percentualAcerto = 0.0;

                if (!identificado) {
                    respondidos.add(
                        new FormRespondidoDTO(
                            f.getId(), f.getTitulo(), false, 0.0
                        )
                    );
                    continue;
                }

                Submissao submissao = subs.get(0);

                TypedQuery<Questao> qQuests = em.createQuery(
                    "SELECT q FROM Questao q WHERE q.idFormulario = :fid",
                    Questao.class
                );
                qQuests.setParameter("fid", f.getId());
                List<Questao> questoes = qQuests.getResultList();

                int totalOpcoes = 0;
                int corretas = 0;

                for (Questao q : questoes) {
                    if (q.getTipo().equalsIgnoreCase("dissertativa")) {
                        continue;
                    }
                    TypedQuery<Opcao> qOps = em.createQuery(
                        "SELECT o FROM Opcao o WHERE o.idQuestao = :qid",
                        Opcao.class
                    );
                    qOps.setParameter("qid", q.getId());
                    List<Opcao> opcoes = qOps.getResultList();

                    for (Opcao op : opcoes) {
                        totalOpcoes++;

                        TypedQuery<Resposta> qResp = em.createQuery(
                            "SELECT r FROM Resposta r WHERE r.idSubmissao = :sid AND r.idOpcao = :oid",
                            Resposta.class
                        );
                        qResp.setParameter("sid", submissao.getId());
                        qResp.setParameter("oid", op.getId());
                        List<Resposta> respostas = qResp.getResultList();

                        boolean alunoMarcado = !respostas.isEmpty();
                        boolean opcaoCorreta = Boolean.TRUE.equals(op.getCorreta());

                        if (q.getTipo().equalsIgnoreCase("vf")) {
                            if (alunoMarcado) {
                                Boolean respostaVF = respostas.get(0).getRespostaVf();

                                if (respostaVF != null && respostaVF.equals(opcaoCorreta)) {
                                    corretas++;
                                }
                            }
                        } else {
                            if (opcaoCorreta && alunoMarcado) {
                                corretas++;
                            } else if (!opcaoCorreta && !alunoMarcado) {
                                corretas++;
                            }
                        }
                    }
                }

                double perc = totalOpcoes == 0 ? 0.0 : (corretas * 100.0 / totalOpcoes);
                long percArredondado = Math.round(perc);

                respondidos.add(
                    new FormRespondidoDTO(
                        f.getId(),
                        f.getTitulo(),
                        identificado,
                        percArredondado
                    )
                );

            }

            return new ProcessoDetalhesDTO(naoRespondidos, respondidos);

        } finally {
            em.close();
        }
    }

}
