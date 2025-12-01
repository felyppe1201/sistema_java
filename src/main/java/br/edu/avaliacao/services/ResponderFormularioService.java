package br.edu.avaliacao.services;

import br.edu.avaliacao.models.Formulario;
import br.edu.avaliacao.models.Questao;
import br.edu.avaliacao.models.Opcao;
import br.edu.avaliacao.config.EntityManagerUtil;

import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.stream.Collectors;

public class ResponderFormularioService {

    public static class FormularioDTO {
        private Long idFormulario;
        private String titulo;
        private List<QuestaoDTO> questoes;

        public FormularioDTO(Long idFormulario, String titulo, List<QuestaoDTO> questoes) {
            this.idFormulario = idFormulario;
            this.titulo = titulo;
            this.questoes = questoes;
        }

        public Long getIdFormulario() { return idFormulario; }
        public String getTitulo() { return titulo; }
        public List<QuestaoDTO> getQuestoes() { return questoes; }

        public static class QuestaoDTO {
            private Long id;
            private String texto;
            private String tipo;
            private boolean obrigatoria;
            private List<OpcaoDTO> opcoes;

            public QuestaoDTO(Long id, String texto, String tipo, boolean obrigatoria, List<OpcaoDTO> opcoes) {
                this.id = id;
                this.texto = texto;
                this.tipo = tipo;
                this.obrigatoria = obrigatoria;
                this.opcoes = opcoes;
            }

            public Long getId() { return id; }
            public String getTexto() { return texto; }
            public String getTipo() { return tipo; }
            public boolean isObrigatoria() { return obrigatoria; }
            public List<OpcaoDTO> getOpcoes() { return opcoes; }
        }

        public static class OpcaoDTO {
            private Long id;
            private String texto;
            private Boolean verdadeiroFalso;

            public OpcaoDTO(Long id, String texto, Boolean verdadeiroFalso) {
                this.id = id;
                this.texto = texto;
                this.verdadeiroFalso = verdadeiroFalso;
            }

            public Long getId() { return id; }
            public String getTexto() { return texto; }
            public Boolean getVerdadeiroFalso() { return verdadeiroFalso; }
        }
    }

    public FormularioDTO montarFormulario(Long formularioId) {

        EntityManager em = EntityManagerUtil.getEntityManager();

        Formulario form = em.find(Formulario.class, formularioId);
        if (form == null) {
            return null;
        }

        List<Questao> questoes = em.createQuery(
                "SELECT q FROM Questao q WHERE q.idFormulario = :fid ORDER BY q.id ASC",
                Questao.class
        )
        .setParameter("fid", formularioId)
        .getResultList();

        List<FormularioDTO.QuestaoDTO> questoesDTO =
            questoes.stream().map(q -> {

                List<Opcao> opcoes = em.createQuery(
                        "SELECT o FROM Opcao o WHERE o.idQuestao = :qid ORDER BY o.id ASC",
                        Opcao.class
                )
                .setParameter("qid", q.getId())
                .getResultList();

                List<FormularioDTO.OpcaoDTO> opcoesDTO =
                    opcoes.stream()
                        .map(o -> new FormularioDTO.OpcaoDTO(
                                o.getId(),
                                o.getTexto(),
                                o.getVf()
                        ))
                        .collect(Collectors.toList());

                return new FormularioDTO.QuestaoDTO(
                        q.getId(),
                        q.getTexto(),
                        q.getTipo(),
                        q.isObrigatoria(),
                        opcoesDTO
                );
            })
            .collect(Collectors.toList());

        return new FormularioDTO(
                form.getId(),
                form.getTitulo(),
                questoesDTO
        );
    }
}
