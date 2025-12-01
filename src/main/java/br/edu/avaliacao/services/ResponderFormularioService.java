package br.edu.avaliacao.services;

import br.edu.avaliacao.models.Formulario;
import br.edu.avaliacao.models.Questao;
import br.edu.avaliacao.models.Opcao;
import br.edu.avaliacao.config.EntityManagerUtil;

import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.stream.Collectors;

public class ResponderFormularioService {

    // ============================================================
    // DTO PRINCIPAL – FORMULÁRIO
    // ============================================================
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

        // ========================================================
        // SUB-DTO – QUESTÃO
        // ========================================================
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

        // ========================================================
        // SUB-DTO – OPÇÃO
        // ========================================================
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

    // ============================================================
    // SERVICE – Montagem do formulário completo
    // ============================================================
    public FormularioDTO montarFormulario(Long formularioId) {

        EntityManager em = EntityManagerUtil.getEntityManager();

        // 1) Buscar formulário
        Formulario form = em.find(Formulario.class, formularioId);
        if (form == null) {
            return null;
        }

        // 2) Buscar questões pelo ID do formulário
        List<Questao> questoes = em.createQuery(
                "SELECT q FROM Questao q WHERE q.idFormulario = :fid ORDER BY q.id ASC",
                Questao.class
        )
        .setParameter("fid", formularioId)
        .getResultList();

        // 3) Converter questões e suas opções
        List<FormularioDTO.QuestaoDTO> questoesDTO =
            questoes.stream().map(q -> {

                // Buscar opções da questão
                List<Opcao> opcoes = em.createQuery(
                        "SELECT o FROM Opcao o WHERE o.idQuestao = :qid ORDER BY o.id ASC",
                        Opcao.class
                )
                .setParameter("qid", q.getId())
                .getResultList();

                // Converter opções → DTO
                List<FormularioDTO.OpcaoDTO> opcoesDTO =
                    opcoes.stream()
                        .map(o -> new FormularioDTO.OpcaoDTO(
                                o.getId(),
                                o.getTexto(),
                                o.getVf()
                        ))
                        .collect(Collectors.toList());

                // Converter questão → DTO
                return new FormularioDTO.QuestaoDTO(
                        q.getId(),
                        q.getTexto(),
                        q.getTipo(),
                        q.isObrigatoria(),
                        opcoesDTO
                );
            })
            .collect(Collectors.toList());

        // 4) Retornar formulário completo
        return new FormularioDTO(
                form.getId(),
                form.getTitulo(),
                questoesDTO
        );
    }
}
