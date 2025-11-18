package br.edu.avaliacao.models;

import java.sql.Timestamp;

public class Submissao {
    private int id;
    private int idFormulario;
    private int idTurma;
    private Integer idUsuario;
    private Timestamp dataEnvio;
    public Submissao() {}
    public Submissao(int id, int idFormulario, int idTurma, Integer idUsuario, Timestamp dataEnvio) { this.id = id; this.idFormulario = idFormulario; this.idTurma = idTurma; this.idUsuario = idUsuario; this.dataEnvio = dataEnvio; }
    public int getId() { return id; } public void setId(int id) { this.id = id; }
    public int getIdFormulario() { return idFormulario; } public void setIdFormulario(int idFormulario) { this.idFormulario = idFormulario; }
    public int getIdTurma() { return idTurma; } public void setIdTurma(int idTurma) { this.idTurma = idTurma; }
    public Integer getIdUsuario() { return idUsuario; } public void setIdUsuario(Integer idUsuario) { this.idUsuario = idUsuario; }
    public Timestamp getDataEnvio() { return dataEnvio; } public void setDataEnvio(Timestamp dataEnvio) { this.dataEnvio = dataEnvio; }
}
