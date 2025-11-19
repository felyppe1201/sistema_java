package br.edu.avaliacao.models;

import jakarta.persistence.*;

@Entity
@Table(name = "questao")
public class Questao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "formulario_id", nullable = false)
    private long idFormulario;

    @Column(nullable = false)
    private String texto;

    @Column(nullable = false)
    private String tipo;

    @Column(nullable = false)
    private boolean obrigatoria;

    public Questao() {}

    public Questao(long id, long idFormulario, String texto, String tipo, boolean obrigatoria) {
        this.id = id;
        this.idFormulario = idFormulario;
        this.texto = texto;
        this.tipo = tipo;
        this.obrigatoria = obrigatoria;
    }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }
    public long getIdFormulario() { return idFormulario; }
    public void setIdFormulario(long idFormulario) { this.idFormulario = idFormulario; }
    public String getTexto() { return texto; }
    public void setTexto(String texto) { this.texto = texto; }
    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }
    public boolean isObrigatoria() { return obrigatoria; }
    public void setObrigatoria(boolean obrigatoria) { this.obrigatoria = obrigatoria; }
}
