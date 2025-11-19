package br.edu.avaliacao.models;

import jakarta.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "submissao")
public class Submissao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "formulario_id", nullable = false)
    private long idFormulario;

    @Column(name = "turma_id", nullable = false)
    private long idTurma;

    @Column(name = "usuario_id")
    private Long idUsuario;

    @Column(name = "data_envio", nullable = false, insertable = false, updatable = false)
    private Timestamp dataEnvio;

    public Submissao() {}

    public Submissao(long id, long idFormulario, long idTurma, Long idUsuario, Timestamp dataEnvio) {
        this.id = id;
        this.idFormulario = idFormulario;
        this.idTurma = idTurma;
        this.idUsuario = idUsuario;
        this.dataEnvio = dataEnvio;
    }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }
    public long getIdFormulario() { return idFormulario; }
    public void setIdFormulario(long idFormulario) { this.idFormulario = idFormulario; }
    public long getIdTurma() { return idTurma; }
    public void setIdTurma(long idTurma) { this.idTurma = idTurma; }
    public Long getIdUsuario() { return idUsuario; }
    public void setIdUsuario(Long idUsuario) { this.idUsuario = idUsuario; }
    public Timestamp getDataEnvio() { return dataEnvio; }
    public void setDataEnvio(Timestamp dataEnvio) { this.dataEnvio = dataEnvio; }
}
