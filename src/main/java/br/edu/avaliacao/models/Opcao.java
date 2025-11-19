package br.edu.avaliacao.models;

import jakarta.persistence.*;

@Entity
@Table(name = "opcao")
public class Opcao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "questao_id", nullable = false)
    private long idQuestao;

    @Column(nullable = false)
    private String texto;

    public Opcao() {}

    public Opcao(long id, long idQuestao, String texto) {
        this.id = id;
        this.idQuestao = idQuestao;
        this.texto = texto;
    }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }
    public long getIdQuestao() { return idQuestao; }
    public void setIdQuestao(long idQuestao) { this.idQuestao = idQuestao; }
    public String getTexto() { return texto; }
    public void setTexto(String texto) { this.texto = texto; }
}
