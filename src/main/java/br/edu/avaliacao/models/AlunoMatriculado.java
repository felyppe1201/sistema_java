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

    @Column(name = "curso_id", nullable = false)
    private long cursoId;

    @Column(columnDefinition = "INT UNSIGNED")
    private Integer periodo;

    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT TRUE")
    private boolean ativo = true;

    // Relação com Usuario
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario", insertable = false, updatable = false)
    private Usuario usuario;

    // Relação com Curso
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "curso_id", insertable = false, updatable = false)
    private Curso curso;

    public AlunoMatriculado() {}

    public AlunoMatriculado(long id, long idUsuario, long cursoId, boolean ativo) {
        this.id = id;
        this.idUsuario = idUsuario;
        this.cursoId = cursoId;
        this.ativo = ativo;
    }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public long getIdUsuario() { return idUsuario; }
    public void setIdUsuario(long idUsuario) { this.idUsuario = idUsuario; }

    public long getCursoId() { return cursoId; }
    public void setCursoId(long cursoId) { this.cursoId = cursoId; }

    public Integer getPeriodo() { return periodo; }
    public void setPeriodo(Integer periodo) { this.periodo = periodo; }

    public boolean isAtivo() { return ativo; }
    public void setAtivo(boolean ativo) { this.ativo = ativo; }

    public Usuario getUsuario() { return usuario; }
    public Curso getCurso() { return curso; }
}
