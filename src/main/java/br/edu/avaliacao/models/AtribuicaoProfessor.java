package br.edu.avaliacao.models;

import jakarta.persistence.*;

@Entity
@Table(name = "atribuicao_professor")
public class AtribuicaoProfessor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "id_turma", nullable = false)
    private long idTurma;

    @Column(name = "id_professor", nullable = false)
    private long idProfessor;

    public AtribuicaoProfessor() {}

    public AtribuicaoProfessor(long id, long idTurma, long idProfessor) {
        this.id = id;
        this.idTurma = idTurma;
        this.idProfessor = idProfessor;
    }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }
    public long getIdTurma() { return idTurma; }
    public void setIdTurma(long idTurma) { this.idTurma = idTurma; }
    public long getIdProfessor() { return idProfessor; }
    public void setIdProfessor(long idProfessor) { this.idProfessor = idProfessor; }
}
