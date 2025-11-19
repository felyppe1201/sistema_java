package br.edu.avaliacao.models;

import jakarta.persistence.*;

@Entity
@Table(name = "disciplina")
public class Disciplina {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "curso_id", nullable = false)
    private long cursoId;

    @Column(nullable = false)
    private String nome;

    @Column(columnDefinition = "INT UNSIGNED")
    private Integer periodo;

    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT TRUE")
    private boolean ativo = true;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "curso_id", insertable = false, updatable = false)
    private Curso curso;

    public Disciplina() {}

    public Disciplina(long id, long cursoId, String nome, boolean ativo) {
        this.id = id;
        this.cursoId = cursoId;
        this.nome = nome;
        this.ativo = ativo;
    }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public long getCursoId() { return cursoId; }
    public void setCursoId(long cursoId) { this.cursoId = cursoId; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public Integer getPeriodo() { return periodo; }
    public void setPeriodo(Integer periodo) { this.periodo = periodo; }

    public boolean isAtivo() { return ativo; }
    public void setAtivo(boolean ativo) { this.ativo = ativo; }

    public Curso getCurso() { return curso; }
}
