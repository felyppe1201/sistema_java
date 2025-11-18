package br.edu.avaliacao.models;

public class ProcessoAvaliativo {
    private int id;
    private String nome;
    private String semestre;
    private String status;
    public ProcessoAvaliativo() {}
    public ProcessoAvaliativo(int id, String nome, String semestre, String status) { this.id = id; this.nome = nome; this.semestre = semestre; this.status = status; }
    public int getId() { return id; } public void setId(int id) { this.id = id; }
    public String getNome() { return nome; } public void setNome(String nome) { this.nome = nome; }
    public String getSemestre() { return semestre; } public void setSemestre(String semestre) { this.semestre = semestre; }
    public String getStatus() { return status; } public void setStatus(String status) { this.status = status; }
}
