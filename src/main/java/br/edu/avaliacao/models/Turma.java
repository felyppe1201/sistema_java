package br.edu.avaliacao.models;

public class Turma {
    private long id;
    private long idDisciplina;
    private String codigoTurma;
    private int numeroVagas;
    private int periodo;
    private int stat;

    public Turma() {}

    public Turma(long id, long idDisciplina, String codigoTurma, int numeroVagas, int periodo, int stat) {
        this.id = id;
        this.idDisciplina = idDisciplina;
        this.codigoTurma = codigoTurma;
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

    public int getNumeroVagas() { return numeroVagas; }
    public void setNumeroVagas(int numeroVagas) { this.numeroVagas = numeroVagas; }

    public int getPeriodo() { return periodo; }
    public void setPeriodo(int periodo) { this.periodo = periodo; }

    public int getStat() { return stat; }
    public void setStat(int stat) { this.stat = stat; }
}
