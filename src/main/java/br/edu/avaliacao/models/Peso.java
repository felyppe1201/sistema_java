package br.edu.avaliacao.models;

import jakarta.persistence.*;

@Entity
@Table(name = "peso")
public class Peso {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "id_questao")
    private Long idQuestao;

    @Column(name = "id_opcao")
    private Long idOpcao;

    @Column(nullable = false)
    private double peso;

    public Peso() {}

    public Peso(long id, Long idQuestao, Long idOpcao, double peso) {
        this.id = id;
        this.idQuestao = idQuestao;
        this.idOpcao = idOpcao;
        this.peso = peso;
    }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }
    public Long getIdQuestao() { return idQuestao; }
    public void setIdQuestao(Long idQuestao) { this.idQuestao = idQuestao; }
    public Long getIdOpcao() { return idOpcao; }
    public void setIdOpcao(Long idOpcao) { this.idOpcao = idOpcao; }
    public double getPeso() { return peso; }
    public void setPeso(double peso) { this.peso = peso; }
}
