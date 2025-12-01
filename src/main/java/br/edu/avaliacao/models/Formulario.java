package br.edu.avaliacao.models;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "formulario")
public class Formulario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "processo_id", nullable = false)
    private long idProcesso;

    @Column(nullable = false)
    private String titulo;

    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT TRUE")
    private boolean identificado = true;

    @Column(nullable = false)
    private boolean ativo = true;

    @Column(name = "stat", nullable = false, columnDefinition = "INT UNSIGNED DEFAULT 1")
    private Integer stat = 1;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "formulario")
    private List<Submissao> submissaoList;

    public Formulario() {}

    public Formulario(long id, long idProcesso, String titulo, boolean identificado, boolean ativo, Integer stat) {
        this.id = id;
        this.idProcesso = idProcesso;
        this.titulo = titulo;
        this.identificado = identificado;
        this.ativo = ativo;
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
    public boolean isAtivo() { return ativo; }
    public void setAtivo(boolean ativo) { this.ativo = ativo; }
    public Integer getStat() { return stat; }
    public void setStat(Integer stat) { this.stat = stat; }
    public List<Submissao> getSubmissaoList() { return submissaoList; }
    public void setSubmissaoList(List<Submissao> submissaoList) { this.submissaoList = submissaoList; }
}
