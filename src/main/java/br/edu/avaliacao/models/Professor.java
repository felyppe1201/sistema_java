package br.edu.avaliacao.models;

public class Professor {
    private int id;
    private int idUsuario;
    public Professor() {}
    public Professor(int id, int idUsuario) { this.id = id; this.idUsuario = idUsuario; }
    public int getId() { return id; } public void setId(int id) { this.id = id; }
    public int getIdUsuario() { return idUsuario; } public void setIdUsuario(int idUsuario) { this.idUsuario = idUsuario; }
}
