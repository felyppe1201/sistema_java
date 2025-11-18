package models;

import java.sql.Timestamp;

public class Participacao {
    private int id;
    private int idFormulario;
    private int idTurma;
    private int idUsuario;
    private Timestamp dataResposta;

    public Participacao() {}

    public Participacao(int id, int idFormulario, int idTurma, int idUsuario, Timestamp dataResposta) {
        this.id = id;
        this.idFormulario = idFormulario;
        this.idTurma = idTurma;
        this.idUsuario = idUsuario;
        this.dataResposta = dataResposta;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getIdFormulario() { return idFormulario; }
    public void setIdFormulario(int idFormulario) { this.idFormulario = idFormulario; }

    public int getIdTurma() { return idTurma; }
    public void setIdTurma(int idTurma) { this.idTurma = idTurma; }

    public int getIdUsuario() { return idUsuario; }
    public void setIdUsuario(int idUsuario) { this.idUsuario = idUsuario; }

    public Timestamp getDataResposta() { return dataResposta; }
    public void setDataResposta(Timestamp dataResposta) { this.dataResposta = dataResposta; }
}
