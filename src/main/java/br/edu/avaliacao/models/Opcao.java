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

    @Column(name = "vf", nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
    private Boolean vf = false;

    @Column(name = "respostavf")
    private Boolean respostavf;

    @Column(name = "correta", columnDefinition = "BOOLEAN DEFAULT FALSE")
    private Boolean correta = false;

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

    public Boolean getVf() { return vf; }
    public void setVf(Boolean vf) { this.vf = vf; }

    public Boolean getRespostavf() { return respostavf; }
    public void setRespostavf(Boolean respostavf) { this.respostavf = respostavf; }

    public Boolean getCorreta() { return correta; }
    public void setCorreta(Boolean correta) { this.correta = correta; }
}