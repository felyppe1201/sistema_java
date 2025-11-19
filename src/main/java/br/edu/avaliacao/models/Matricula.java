package br.edu.avaliacao.models;

import jakarta.persistence.*;

@Entity
@Table(name = "matricula")
public class Matricula {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "turma_id", nullable = false)
    private long idTurma;

    @Column(name = "aluno_id", nullable = false)
    private long idAluno;

    @Column(nullable = false)
    private boolean ativo;

    @Column(name = "stat", nullable = false, columnDefinition = "INT UNSIGNED DEFAULT 1")
    private Integer stat = 1;

    public Matricula() {}

    public Matricula(long id, long idTurma, long idAluno, boolean ativo, Integer stat) {
        this.id = id;
        this.idTurma = idTurma;
        this.idAluno = idAluno;
        this.ativo = ativo;
        this.stat = stat;
    }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }
    public long getIdTurma() { return idTurma; }
    public void setIdTurma(long idTurma) { this.idTurma = idTurma; }
    public long getIdAluno() { return idAluno; }
    public void setIdAluno(long idAluno) { this.idAluno = idAluno; }
    public boolean isAtivo() { return ativo; }
    public void setAtivo(boolean ativo) { this.ativo = ativo; }
    public Integer getStat() { return stat; }
    public void setStat(Integer stat) { this.stat = stat; }
}