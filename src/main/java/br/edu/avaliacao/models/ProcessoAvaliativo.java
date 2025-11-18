package br.edu.avaliacao.models;

public class ProcessoAvaliativo {
    private long id;
    private String nome;
    private int periodo;
    private int stat;

    public ProcessoAvaliativo() {}

    public ProcessoAvaliativo(long id, String nome, int periodo, int stat) {
        this.id = id;
        this.nome = nome;
        this.periodo = periodo;
        this.stat = stat;
    }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public int getPeriodo() { return periodo; }
    public void setPeriodo(int periodo) { this.periodo = periodo; }

    public int getStat() { return stat; }
    public void setStat(int stat) { this.stat = stat; }
}
