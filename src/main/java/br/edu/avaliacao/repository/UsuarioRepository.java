package br.edu.avaliacao.repository;

import br.edu.avaliacao.models.Usuario;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UsuarioRepository {
    protected Connection conn;

    public UsuarioRepository(Connection conn) {
        this.conn = conn;
    }

    public void save(Usuario u) throws SQLException {
        String sql = "INSERT INTO Usuario (id, nome, email, senha, cargo, ativo, stat) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement st = conn.prepareStatement(sql)) {
            st.setLong(1, u.getId());
            st.setString(2, u.getNome());
            st.setString(3, u.getEmail());
            st.setString(4, u.getSenha());
            st.setString(5, u.getCargo());        
            st.setBoolean(6, u.isAtivo());        
            st.setInt(7, u.getStat());
            st.executeUpdate();
        }
    }

    public Usuario findById(long id) throws SQLException {
        String sql = "SELECT id, nome, email, senha, cargo, ativo, stat FROM Usuario WHERE id = ?";
        try (PreparedStatement st = conn.prepareStatement(sql)) {
            st.setLong(1, id);
            try (ResultSet rs = st.executeQuery()) {
                if (rs.next()) {
                    Usuario u = new Usuario();
                    u.setId(rs.getLong("id"));
                    u.setNome(rs.getString("nome"));
                    u.setEmail(rs.getString("email"));
                    u.setSenha(rs.getString("senha"));
                    u.setCargo(rs.getString("cargo"));
                    u.setAtivo(rs.getBoolean("ativo")); // set usando boolean
                    u.setStat(rs.getInt("stat"));
                    return u;
                }
            }
        }
        return null;
    }

    public List<Usuario> findAll() throws SQLException {
        String sql = "SELECT id, nome, email, senha, cargo, ativo, stat FROM Usuario";
        List<Usuario> list = new ArrayList<>();
        try (PreparedStatement st = conn.prepareStatement(sql);
             ResultSet rs = st.executeQuery()) {
            while (rs.next()) {
                Usuario u = new Usuario();
                u.setId(rs.getLong("id"));
                u.setNome(rs.getString("nome"));
                u.setEmail(rs.getString("email"));
                u.setSenha(rs.getString("senha"));
                u.setCargo(rs.getString("cargo"));
                u.setAtivo(rs.getBoolean("ativo"));
                u.setStat(rs.getInt("stat"));
                list.add(u);
            }
        }
        return list;
    }

    public void update(Usuario u) throws SQLException {
        String sql = "UPDATE Usuario SET nome = ?, email = ?, senha = ?, cargo = ?, ativo = ?, stat = ? WHERE id = ?";
        try (PreparedStatement st = conn.prepareStatement(sql)) {
            st.setString(1, u.getNome());
            st.setString(2, u.getEmail());
            st.setString(3, u.getSenha());
            st.setString(4, u.getCargo());
            st.setBoolean(5, u.isAtivo());
            st.setInt(6, u.getStat());
            st.setLong(7, u.getId());
            st.executeUpdate();
        }
    }

    public void delete(long id) throws SQLException {
        String sql = "DELETE FROM Usuario WHERE id = ?";
        try (PreparedStatement st = conn.prepareStatement(sql)) {
            st.setLong(1, id);
            st.executeUpdate();
        }
    }

    /**
     * Autentica (simples). Retorna true se existir usuário com email e senha igual.
     * Não faz hashing — se você usa hash, ajuste aqui.
     */
    public boolean authenticate(String email, String senha) throws SQLException {
        String sql = "SELECT id FROM Usuario WHERE email = ? AND senha = ?";
        try (PreparedStatement st = conn.prepareStatement(sql)) {
            st.setString(1, email);
            st.setString(2, senha);
            try (ResultSet rs = st.executeQuery()) {
                return rs.next();
            }
        }
    }
}

