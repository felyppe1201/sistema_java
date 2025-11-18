package models;

public class Resposta {
    private int id;
    private int idSubmissao;
    private int idQuestao;
    private Integer idOpcao;
    private String texto;

    public Resposta() {}

    public Resposta(int id, int idSubmissao, int idQuestao, Integer idOpcao, String texto) {
        this.id = id;
        this.idSubmissao = idSubmissao;
        this.idQuestao = idQuestao;
        this.idOpcao = idOpcao;
        this.texto = texto;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getIdSubmissao() { return idSubmissao; }
    public void setIdSubmissao(int idSubmissao) { this.idSubmissao = idSubmissao; }

    public int getIdQuestao() { return idQuestao; }
    public void setIdQuestao(int idQuestao) { this.idQuestao = idQuestao; }

    public Integer getIdOpcao() { return idOpcao; }
    public void setIdOpcao(Integer idOpcao) { this.idOpcao = idOpcao; }

    public String getTexto() { return texto; }
    public void setTexto(String texto) { this.texto = texto; }
}
