package br.edu.avaliacao.models;

public class Opcao {
    private int id;
    private int idQuestao;
    private String texto;
    public Opcao() {}
    public Opcao(int id, int idQuestao, String texto) { this.id = id; this.idQuestao = idQuestao; this.texto = texto; }
    public int getId() { return id; } public void setId(int id) { this.id = id; }
    public int getIdQuestao() { return idQuestao; } public void setIdQuestao(int idQuestao) { this.idQuestao = idQuestao; }
    public String getTexto() { return texto; } public void setTexto(String texto) { this.texto = texto; }
}
