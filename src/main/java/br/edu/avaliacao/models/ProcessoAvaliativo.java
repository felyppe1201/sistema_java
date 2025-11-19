package br.edu.avaliacao.models;

import jakarta.persistence.*;

@Entity
@Table(name = "processo_avaliativo")
public class ProcessoAvaliativo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id; // long conforme solicitado

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private boolean ativo = true;

    @Column(nullable = false)
    private int periodo; // NOVO: Campo período

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "turma_id", nullable = false)
    private Turma turma;

    @Column(name = "stat", nullable = false, columnDefinition = "INT UNSIGNED DEFAULT 1")
    private Integer stat = 1; // NOVO: Campo stat

    public ProcessoAvaliativo() {}

    public ProcessoAvaliativo(long id, String nome, boolean ativo, int periodo, Turma turma, Integer stat) {
        this.id = id;
        this.nome = nome;
        this.ativo = ativo;
        this.periodo = periodo;
        this.turma = turma;
        this.stat = stat;
    }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public boolean isAtivo() { return ativo; }
    public void setAtivo(boolean ativo) { this.ativo = ativo; }

    public int getPeriodo() { return periodo; } // Getter para período
    public void setPeriodo(int periodo) { this.periodo = periodo; } // Setter para período

    public Turma getTurma() { return turma; }
    public void setTurma(Turma turma) { this.turma = turma; }

    public Integer getStat() { return stat; } // Getter para stat
    public void setStat(Integer stat) { this.stat = stat; } // Setter para stat
}