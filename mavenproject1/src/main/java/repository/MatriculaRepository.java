
package repository;

import models.Matricula;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class MatriculaRepository {
    protected Connection conn;
    public MatriculaRepository(Connection conn){{ this.conn = conn; }}

    public void save(Matricula obj) throws SQLException {{}}
    public Matricula findById(int id) throws SQLException {{ return null; }}
    public List<Matricula> findAll() throws SQLException {{ return null; }}
    public void update(Matricula obj) throws SQLException {{}}
    public void delete(int id) throws SQLException {{}}
}
