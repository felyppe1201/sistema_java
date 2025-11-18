package models;

public class Turma {
    private int id;
    private int idDisciplina;
    private String codigoTurma;
    private int numeroVagas;
    private String periodo;
    private String status;

    public Turma() {}

    public Turma(int id, int idDisciplina, String codigoTurma, int numeroVagas, String periodo, String status) {
        this.id = id;
        this.idDisciplina = idDisciplina;
        this.codigoTurma = codigoTurma;
        this.numeroVagas = numeroVagas;
        this.periodo = periodo;
        this.status = status;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getIdDisciplina() { return idDisciplina; }
    public void setIdDisciplina(int idDisciplina) { this.idDisciplina = idDisciplina; }

    public String getCodigoTurma() { return codigoTurma; }
    public void setCodigoTurma(String codigoTurma) { this.codigoTurma = codigoTurma; }

    public int getNumeroVagas() { return numeroVagas; }
    public void setNumeroVagas(int numeroVagas) { this.numeroVagas = numeroVagas; }

    public String getPeriodo() { return periodo; }
    public void setPeriodo(String periodo) { this.periodo = periodo; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
