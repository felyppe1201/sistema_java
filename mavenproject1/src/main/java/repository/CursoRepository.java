
package repository;

import models.Curso;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class CursoRepository {
    protected Connection conn;
    public CursoRepository(Connection conn){{ this.conn = conn; }}

    public void save(Curso obj) throws SQLException {{}}
    public Curso findById(int id) throws SQLException {{ return null; }}
    public List<Curso> findAll() throws SQLException {{ return null; }}
    public void update(Curso obj) throws SQLException {{}}
    public void delete(int id) throws SQLException {{}}
}
