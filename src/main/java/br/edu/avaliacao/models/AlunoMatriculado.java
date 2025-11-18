package br.edu.avaliacao.models;

public class AlunoMatriculado {
    private long id;
    private long idUsuario;
    private long idCurso;

    public AlunoMatriculado() {}

    public AlunoMatriculado(long id, long idUsuario, long idCurso) {
        this.id = id;
        this.idUsuario = idUsuario;
        this.idCurso = idCurso;
    }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public long getIdUsuario() { return idUsuario; }
    public void setIdUsuario(long idUsuario) { this.idUsuario = idUsuario; }

    public long getIdCurso() { return idCurso; }
    public void setIdCurso(long idCurso) { this.idCurso = idCurso; }
}
