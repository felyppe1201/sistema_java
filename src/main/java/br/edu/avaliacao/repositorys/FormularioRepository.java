
package br.edu.avaliacao.repositorys;

import br.edu.avaliacao.models.Formulario;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class FormularioRepository {
    protected Connection conn;
    public FormularioRepository(Connection conn){ this.conn = conn; }

    public void save(Formulario obj) throws SQLException {}
    public Formulario findById(long id) throws SQLException { return null; }
    public List<Formulario> findAll() throws SQLException { return null; }
    public void update(Formulario obj) throws SQLException {}
    public void delete(long id) throws SQLException {}
}
