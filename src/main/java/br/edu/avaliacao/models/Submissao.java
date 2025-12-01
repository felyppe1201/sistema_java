package br.edu.avaliacao.models;

import jakarta.persistence.*;
import java.sql.Timestamp;
import java.math.BigDecimal;

@Entity
@Table(name = "submissao")
public class Submissao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "formulario_id", nullable = false)
    private long idFormulario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "formulario_id", referencedColumnName = "id", insertable = false, updatable = false)
    private Formulario formulario;

    @Column(name = "turma_id", nullable = false)
    private long idTurma;

    @Column(name = "usuario_id")
    private Long idUsuario;

    @Column(name = "data_envio", nullable = false, insertable = false, updatable = false)
    private Timestamp dataEnvio;

    @Column(name = "nota", precision = 5, scale = 2)
    private BigDecimal nota;

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

    public Formulario getFormulario() { return formulario; }
    public void setFormulario(Formulario formulario) { this.formulario = formulario; }

    public long getIdTurma() { return idTurma; }
    public void setIdTurma(long idTurma) { this.idTurma = idTurma; }

    public Long getIdUsuario() { return idUsuario; }
    public void setIdUsuario(Long idUsuario) { this.idUsuario = idUsuario; }

    public Timestamp getDataEnvio() { return dataEnvio; }
    public void setDataEnvio(Timestamp dataEnvio) { this.dataEnvio = dataEnvio; }

    public BigDecimal getNota() { return nota; }
    public void setNota(BigDecimal nota) { this.nota = nota; }
}
