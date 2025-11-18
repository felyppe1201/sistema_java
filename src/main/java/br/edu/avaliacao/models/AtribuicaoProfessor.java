package br.edu.avaliacao.models;

public class AtribuicaoProfessor {
    private int id;
    private int idTurma;
    private int idProfessor;
    public AtribuicaoProfessor() {}
    public AtribuicaoProfessor(int id, int idTurma, int idProfessor) { this.id = id; this.idTurma = idTurma; this.idProfessor = idProfessor; }
    public int getId() { return id; } public void setId(int id) { this.id = id; }
    public int getIdTurma() { return idTurma; } public void setIdTurma(int idTurma) { this.idTurma = idTurma; }
    public int getIdProfessor() { return idProfessor; } public void setIdProfessor(int idProfessor) { this.idProfessor = idProfessor; }
}
