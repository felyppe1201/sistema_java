package br.edu.avaliacao.models;

import jakarta.persistence.*;

@Entity
@Table(name = "aluno_matriculado")
public class AlunoMatriculado {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "id_usuario", nullable = false)
    private long idUsuario;

    @Column(name = "id_curso", nullable = false)
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
