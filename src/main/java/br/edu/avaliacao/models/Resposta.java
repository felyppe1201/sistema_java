package br.edu.avaliacao.models;

public class Resposta {
    private long id;
    private long idSubmissao;
    private long idQuestao;
    private Long idOpcao;
    private String texto;

    public Resposta() {}

    public Resposta(long id, long idSubmissao, long idQuestao, Long idOpcao, String texto) {
        this.id = id;
        this.idSubmissao = idSubmissao;
        this.idQuestao = idQuestao;
        this.idOpcao = idOpcao;
        this.texto = texto;
    }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public long getIdSubmissao() { return idSubmissao; }
    public void setIdSubmissao(long idSubmissao) { this.idSubmissao = idSubmissao; }

    public long getIdQuestao() { return idQuestao; }
    public void setIdQuestao(long idQuestao) { this.idQuestao = idQuestao; }

    public Long getIdOpcao() { return idOpcao; }
    public void setIdOpcao(Long idOpcao) { this.idOpcao = idOpcao; }

    public String getTexto() { return texto; }
    public void setTexto(String texto) { this.texto = texto; }
}
