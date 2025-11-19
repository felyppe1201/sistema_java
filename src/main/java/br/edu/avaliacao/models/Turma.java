package br.edu.avaliacao.models;

import jakarta.persistence.*;

@Entity
@Table(name = "turma")
public class Turma {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "disciplina_id", nullable = false)
    private long idDisciplina;

    @Column(name = "codigo_turma", nullable = false)
    private String codigoTurma;

    @Column(nullable = false)
    private boolean ativo;

    @Column(name = "numero_vagas", nullable = false)
    private int numeroVagas;

    @Column(nullable = false)
    private int periodo;

    @Column(name = "stat", nullable = false, columnDefinition = "INT UNSIGNED DEFAULT 1")
    private Integer stat = 1;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "disciplina_id", insertable = false, updatable = false)
    private Disciplina disciplina;

    public Turma() {}

    public Turma(long id, long idDisciplina, String codigoTurma, boolean ativo, int numeroVagas, int periodo, Integer stat) {
        this.id = id;
        this.idDisciplina = idDisciplina;
        this.codigoTurma = codigoTurma;
        this.ativo = ativo;
        this.numeroVagas = numeroVagas;
        this.periodo = periodo;
        this.stat = stat;
    }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public long getIdDisciplina() { return idDisciplina; }
    public void setIdDisciplina(long idDisciplina) { this.idDisciplina = idDisciplina; }

    public String getCodigoTurma() { return codigoTurma; }
    public void setCodigoTurma(String codigoTurma) { this.codigoTurma = codigoTurma; }

    public boolean isAtivo() { return ativo; }
    public void setAtivo(boolean ativo) { this.ativo = ativo; }

    public int getNumeroVagas() { return numeroVagas; }
    public void setNumeroVagas(int numeroVagas) { this.numeroVagas = numeroVagas; }

    public int getPeriodo() { return periodo; }
    public void setPeriodo(int periodo) { this.periodo = periodo; }

    public Integer getStat() { return stat; }
    public void setStat(Integer stat) { this.stat = stat; }

    public Disciplina getDisciplina() { return disciplina; }
}
