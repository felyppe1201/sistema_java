package br.edu.avaliacao.models;

import jakarta.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "Usuario")
public class Usuario implements Serializable {

    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "nome", nullable = false, length = 255)
    private String nome;

    @Column(name = "email", nullable = false, unique = true, length = 255)
    private String email;

    @Column(name = "senha", nullable = false, length = 128)
    private String senha;

    @Column(name = "cargo", nullable = false, length = 10)
    private String cargo; // 'alu','prof','coord','adm'

    @Column(name = "ativo", nullable = false)
    private Boolean ativo = true;

    @Column(name = "stat", nullable = false)
    private Integer stat = 1;

    public Usuario() {}

    // getters e setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }

    public String getCargo() { return cargo; }
    public void setCargo(String cargo) { this.cargo = cargo; }

    public Boolean getAtivo() { return ativo; }
    public void setAtivo(Boolean ativo) { this.ativo = ativo; }

    public Integer getStat() { return stat; }
    public void setStat(Integer stat) { this.stat = stat; }
}
