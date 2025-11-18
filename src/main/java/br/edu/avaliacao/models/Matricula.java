package br.edu.avaliacao.models;

public class Matricula {
    private int id;
    private int idTurma;
    private int idAluno;
    private int idCurso;
    private String status;
    public Matricula() {}
    public Matricula(int id, int idTurma, int idAluno, int idCurso, String status) { this.id = id; this.idTurma = idTurma; this.idAluno = idAluno; this.idCurso = idCurso; this.status = status; }
    public int getId() { return id; } public void setId(int id) { this.id = id; }
    public int getIdTurma() { return idTurma; } public void setIdTurma(int idTurma) { this.idTurma = idTurma; }
    public int getIdAluno() { return idAluno; } public void setIdAluno(int idAluno) { this.idAluno = idAluno; }
    public int getIdCurso() { return idCurso; } public void setIdCurso(int idCurso) { this.idCurso = idCurso; }
    public String getStatus() { return status; } public void setStatus(String status) { this.status = status; }
}
