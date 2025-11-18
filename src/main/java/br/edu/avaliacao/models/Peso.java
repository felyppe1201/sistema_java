package br.edu.avaliacao.models;

public class Peso {
    private int id;
    private Integer idQuestao;
    private Integer idOpcao;
    private String escopo;
    private double valor;
    public Peso() {}
    public Peso(int id, Integer idQuestao, Integer idOpcao, String escopo, double valor) { this.id = id; this.idQuestao = idQuestao; this.idOpcao = idOpcao; this.escopo = escopo; this.valor = valor; }
    public int getId() { return id; } public void setId(int id) { this.id = id; }
    public Integer getIdQuestao() { return idQuestao; } public void setIdQuestao(Integer idQuestao) { this.idQuestao = idQuestao; }
    public Integer getIdOpcao() { return idOpcao; } public void setIdOpcao(Integer idOpcao) { this.idOpcao = idOpcao; }
    public String getEscopo() { return escopo; } public void setEscopo(String escopo) { this.escopo = escopo; }
    public double getValor() { return valor; } public void setValor(double valor) { this.valor = valor; }
}
