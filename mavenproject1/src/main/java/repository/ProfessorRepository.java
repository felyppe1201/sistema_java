
package repository;

import models.Professor;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class ProfessorRepository {
    protected Connection conn;
    public ProfessorRepository(Connection conn){{ this.conn = conn; }}

    public void save(Professor obj) throws SQLException {{}}
    public Professor findById(int id) throws SQLException {{ return null; }}
    public List<Professor> findAll() throws SQLException {{ return null; }}
    public void update(Professor obj) throws SQLException {{}}
    public void delete(int id) throws SQLException {{}}
}
