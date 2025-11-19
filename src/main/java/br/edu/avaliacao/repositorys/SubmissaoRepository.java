
package br.edu.avaliacao.repositorys;

import br.edu.avaliacao.models.Submissao;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class SubmissaoRepository {
    protected Connection conn;
    public SubmissaoRepository(Connection conn){ this.conn = conn; }

    public void save(Submissao obj) throws SQLException {}
    public Submissao findById(long id) throws SQLException { return null; }
    public List<Submissao> findAll() throws SQLException { return null; }
    public void update(Submissao obj) throws SQLException {}
    public void delete(long id) throws SQLException {}
}
