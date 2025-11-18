
package br.edu.avaliacao.repository;

import br.edu.avaliacao.models.Peso;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class PesoRepository {
    protected Connection conn;
    public PesoRepository(Connection conn){ this.conn = conn; }

    public void save(Peso obj) throws SQLException {}
    public Peso findById(long id) throws SQLException { return null; }
    public List<Peso> findAll() throws SQLException { return null; }
    public void update(Peso obj) throws SQLException {}
    public void delete(long id) throws SQLException {}
}
