package br.edu.avaliacao.models;

import jakarta.persistence.*;

@Entity
@Table(name = "resposta")
public class Resposta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "submissao_id", nullable = false)
    private long idSubmissao;

    @Column(name = "questao_id", nullable = false)
    private long idQuestao;

    @Column(name = "opcao_id")
    private Long idOpcao;

    @Column(columnDefinition = "TEXT")
    private String texto;

    public Resposta() {}

    public Resposta(long id, long idSubmissao, long idQuestao, Long idOpcao, String texto) {
        this.id = id;
        this.idSubmissao = idSubmissao;
        this.idQuestao = idQuestao;
        this.idOpcao = idOpcao;
        this.texto = texto;
    }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }
    public long getIdSubmissao() { return idSubmissao; }
    public void setIdSubmissao(long idSubmissao) { this.idSubmissao = idSubmissao; }
    public long getIdQuestao() { return idQuestao; }
    public void setIdQuestao(long idQuestao) { this.idQuestao = idQuestao; }
    public Long getIdOpcao() { return idOpcao; }
    public void setIdOpcao(Long idOpcao) { this.idOpcao = idOpcao; }
    public String getTexto() { return texto; }
    public void setTexto(String texto) { this.texto = texto; }
}
