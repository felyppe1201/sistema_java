package br.edu.avaliacao.models;

import jakarta.persistence.*;

@Entity
@Table(name = "processo_avaliativo")
public class ProcessoAvaliativo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private boolean ativo;

    @Column(nullable = false)
    private int periodo;

    @Column(name = "stat", nullable = false, columnDefinition = "INT UNSIGNED DEFAULT 1")
    private Integer stat = 1;

    public ProcessoAvaliativo() {}

    public ProcessoAvaliativo(long id, String nome, boolean ativo, int periodo, Integer stat) {
        this.id = id;
        this.nome = nome;
        this.ativo = ativo;
        this.periodo = periodo;
        this.stat = stat;
    }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public boolean isAtivo() { return ativo; }
    public void setAtivo(boolean ativo) { this.ativo = ativo; }
    public int getPeriodo() { return periodo; }
    public void setPeriodo(int periodo) { this.periodo = periodo; }
    public Integer getStat() { return stat; }
    public void setStat(Integer stat) { this.stat = stat; }
}