package br.edu.avaliacao.models;

public class AtribuicaoProfessor {
    private long id;
    private long idTurma;
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
