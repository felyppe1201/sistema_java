
package br.edu.avaliacao.repository;

import br.edu.avaliacao.models.Opcao;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class OpcaoRepository {
    protected Connection conn;
    public OpcaoRepository(Connection conn){ this.conn = conn; }

    public void save(Opcao obj) throws SQLException {}
    public Opcao findById(long id) throws SQLException { return null; }
    public List<Opcao> findAll() throws SQLException { return null; }
    public void update(Opcao obj) throws SQLException {}
    public void delete(long id) throws SQLException {}
}
