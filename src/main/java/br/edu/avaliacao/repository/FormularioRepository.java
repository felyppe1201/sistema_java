
package br.edu.avaliacao.repository;

import br.edu.avaliacao.models.Formulario;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class FormularioRepository {
    protected Connection conn;
    public FormularioRepository(Connection conn){{ this.conn = conn; }}

    public void save(Formulario obj) throws SQLException {{}}
    public Formulario findById(int id) throws SQLException {{ return null; }}
    public List<Formulario> findAll() throws SQLException {{ return null; }}
    public void update(Formulario obj) throws SQLException {{}}
    public void delete(int id) throws SQLException {{}}
}
