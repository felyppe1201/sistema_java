package br.edu.avaliacao.models;

public class Peso {
    private long id;
    private Long idQuestao;
    private Long idOpcao;
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
