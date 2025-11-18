package br.edu.avaliacao.models;

import jakarta.persistence.*;

@Entity
@Table(name = "matricula")
public class Matricula {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "id_turma", nullable = false)
    private long idTurma;

    @Column(name = "id_aluno", nullable = false)
    private long idAluno;

    @Column(nullable = false)
    private int stat;

    public Matricula() {}

    public Matricula(long id, long idTurma, long idAluno, int stat) {
        this.id = id;
        this.idTurma = idTurma;
        this.idAluno = idAluno;
        this.stat = stat;
    }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }
    public long getIdTurma() { return idTurma; }
    public void setIdTurma(long idTurma) { this.idTurma = idTurma; }
    public long getIdAluno() { return idAluno; }
    public void setIdAluno(long idAluno) { this.idAluno = idAluno; }
    public int getStat() { return stat; }
    public void setStat(int stat) { this.stat = stat; }
}
