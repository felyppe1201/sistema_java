package br.edu.avaliacao.models;

import jakarta.persistence.*;

@Entity
@Table(name = "AtribuicaoProfessor")
public class AtribuicaoProfessor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "turma_id", nullable = false)
    private Turma turma;

    @ManyToOne
    @JoinColumn(name = "professor_id", nullable = false)
    private Usuario professor;

    public AtribuicaoProfessor() {}

    public AtribuicaoProfessor(long id, Turma turma, Usuario professor) {
        this.id = id;
        this.turma = turma;
        this.professor = professor;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Turma getTurma() {
        return turma;
    }

    public void setTurma(Turma turma) {
        this.turma = turma;
    }

    public Usuario getProfessor() {
        return professor;
    }

    public void setProfessor(Usuario professor) {
        this.professor = professor;
    }
}