package br.edu.avaliacao.models;

import jakarta.persistence.*;

@Entity
@Table(name = "disciplina")
public class Disciplina {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "id_curso", nullable = false)
    private long idCurso;

    @Column(nullable = false)
    private String nome;

    public Disciplina() {}
    public Disciplina(long id, long idCurso, String nome) { this.id = id; this.idCurso = idCurso; this.nome = nome; }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }
    public long getIdCurso() { return idCurso; }
    public void setIdCurso(long idCurso) { this.idCurso = idCurso; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
}
