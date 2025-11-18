package models;

public class Formulario {
    private int id;
    private int idProcesso;
    private String titulo;
    private boolean identificado;
    private String status;

    public Formulario() {}

    public Formulario(int id, int idProcesso, String titulo, boolean identificado, String status) {
        this.id = id;
        this.idProcesso = idProcesso;
        this.titulo = titulo;
        this.identificado = identificado;
        this.status = status;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getIdProcesso() { return idProcesso; }
    public void setIdProcesso(int idProcesso) { this.idProcesso = idProcesso; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public boolean isIdentificado() { return identificado; }
    public void setIdentificado(boolean identificado) { this.identificado = identificado; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
