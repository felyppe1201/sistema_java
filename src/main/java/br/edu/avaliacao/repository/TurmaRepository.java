
package br.edu.avaliacao.repository;

import br.edu.avaliacao.models.Turma;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class TurmaRepository {
    protected Connection conn;
    public TurmaRepository(Connection conn){ this.conn = conn; }

    public void save(Turma obj) throws SQLException {}
    public Turma findById(long id) throws SQLException { return null; }
    public List<Turma> findAll() throws SQLException { return null; }
    public void update(Turma obj) throws SQLException {}
    public void delete(long id) throws SQLException {}
}
