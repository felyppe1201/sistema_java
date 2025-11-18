package br.edu.avaliacao.models;

public class Formulario {
    private long id;
    private long idProcesso;
    private String titulo;
    private boolean identificado;
    private int stat;

    public Formulario() {}

    public Formulario(long id, long idProcesso, String titulo, boolean identificado, int stat) {
        this.id = id;
        this.idProcesso = idProcesso;
        this.titulo = titulo;
        this.identificado = identificado;
        this.stat = stat;
    }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public long getIdProcesso() { return idProcesso; }
    public void setIdProcesso(long idProcesso) { this.idProcesso = idProcesso; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public boolean isIdentificado() { return identificado; }
    public void setIdentificado(boolean identificado) { this.identificado = identificado; }

    public int getStat() { return stat; }
    public void setStat(int stat) { this.stat = stat; }
}
