
package repository;


import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;
import models.Usuario;
import java.sql.*;
import java.util.*;

public class UsuarioRepository {
    protected Connection conn;
    public UsuarioRepository(Connection conn){ this.conn = conn; }

    public void save(Usuario u) throws SQLException {
        String sql = "INSERT INTO usuario (nome,email,senha,tipo,status) VALUES (?,?,?,?)";
        try (PreparedStatement st = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            st.setString(1, u.getNome());
            st.setString(2, u.getEmail());
            st.setString(3, u.getSenha());
            st.setString(4, u.getTipo());
            st.setString(5, u.getStatus());
            st.executeUpdate();
            try (ResultSet rs = st.getGeneratedKeys()) {
                if (rs.next()) u.setId(rs.getInt(1));
            }
        }
    }

    public Usuario findById(int id) throws SQLException {
        String sql = "SELECT id,nome,email,senha,tipo,status FROM usuario WHERE id = ?";
        try (PreparedStatement st = conn.prepareStatement(sql)) {
            st.setInt(1, id);
            try (ResultSet rs = st.executeQuery()) {
                if (rs.next()) {
                    Usuario u = new Usuario();
                    u.setId(rs.getInt("id"));
                    u.setNome(rs.getString("nome"));
                    u.setEmail(rs.getString("email"));
                    u.setSenha(rs.getString("senha"));
                    u.setTipo(rs.getString("tipo"));
                    u.setStatus(rs.getString("status"));
                    return u;
                }
            }
        }
        return null;
    }

    public boolean authenticate(String email, String senha) throws SQLException {
        String sql = "SELECT id FROM usuario WHERE email = ? AND senha = ?";
        try (PreparedStatement st = conn.prepareStatement(sql)) {
            st.setString(1, email);
            st.setString(2, senha);
            try (ResultSet rs = st.executeQuery()) {
                return rs.next();
            }
        }
    }
}
