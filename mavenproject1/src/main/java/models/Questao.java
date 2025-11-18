package models;

public class Questao {
    private int id;
    private int idFormulario;
    private String texto;
    private String tipo;
    private boolean obrigatoria;

    public Questao() {}

    public Questao(int id, int idFormulario, String texto, String tipo, boolean obrigatoria) {
        this.id = id;
        this.idFormulario = idFormulario;
        this.texto = texto;
        this.tipo = tipo;
        this.obrigatoria = obrigatoria;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getIdFormulario() { return idFormulario; }
    public void setIdFormulario(int idFormulario) { this.idFormulario = idFormulario; }

    public String getTexto() { return texto; }
    public void setTexto(String texto) { this.texto = texto; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public boolean isObrigatoria() { return obrigatoria; }
    public void setObrigatoria(boolean obrigatoria) { this.obrigatoria = obrigatoria; }
}
