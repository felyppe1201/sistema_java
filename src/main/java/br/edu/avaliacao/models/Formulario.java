package br.edu.avaliacao.models;

import jakarta.persistence.*;

@Entity
@Table(name = "formulario")
public class Formulario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; 

    @Column(name = "processo_id", nullable = false) 
    private Long idProcesso; 

    @Column(nullable = false)
    private String titulo;

    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT TRUE")
    private boolean identificado = true;

    @Column(nullable = false)
    private boolean ativo = true;

    @Column(name = "stat", nullable = false, columnDefinition = "INT UNSIGNED DEFAULT 1")
    private Integer stat = 1;

    public Formulario() {}

    public Formulario(Long idProcesso, String titulo, boolean identificado, boolean ativo, Integer stat) {
        this.idProcesso = idProcesso;
        this.titulo = titulo;
        this.identificado = identificado;
        this.ativo = ativo;
        this.stat = stat;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getIdProcesso() { return idProcesso; }
    public void setIdProcesso(Long idProcesso) { this.idProcesso = idProcesso; }
    
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public boolean isIdentificado() { return identificado; }
    public void setIdentificado(boolean identificado) {this.identificado = identificado;}
    public boolean isAtivo() { return ativo; }
    public void setAtivo(boolean ativo) { this.ativo = ativo; }
    public Integer getStat() { return stat; }
    public void setStat(Integer stat) { this.stat = stat; }
}