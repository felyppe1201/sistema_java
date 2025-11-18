
package repository;

import models.Disciplina;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class DisciplinaRepository {
    protected Connection conn;
    public DisciplinaRepository(Connection conn){{ this.conn = conn; }}

    public void save(Disciplina obj) throws SQLException {{}}
    public Disciplina findById(int id) throws SQLException {{ return null; }}
    public List<Disciplina> findAll() throws SQLException {{ return null; }}
    public void update(Disciplina obj) throws SQLException {{}}
    public void delete(int id) throws SQLException {{}}
}
