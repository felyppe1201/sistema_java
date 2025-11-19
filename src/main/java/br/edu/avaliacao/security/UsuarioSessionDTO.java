package br.edu.avaliacao.security;

public class UsuarioSessionDTO {
    private long id;
    private String nome;
    private String email;
    private String cargo;
    private int stat;

    public UsuarioSessionDTO(long id, String nome, String email, String cargo, int stat) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.cargo = cargo;
        this.stat = stat;
    }

    public long getId() { return id; }
    public String getNome() { return nome; }
    public String getEmail() { return email; }
    public String getCargo() { return cargo; }
    public int getStat() { return stat; }
    public void setEmail(String email) {
        this.email = email;
    }
}
