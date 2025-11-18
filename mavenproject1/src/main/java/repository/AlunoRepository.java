
package repository;

import models.Aluno;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class AlunoRepository {
    protected Connection conn;
    public AlunoRepository(Connection conn){{ this.conn = conn; }}

    public void save(Aluno obj) throws SQLException {{}}
    public Aluno findById(int id) throws SQLException {{ return null; }}
    public List<Aluno> findAll() throws SQLException {{ return null; }}
    public void update(Aluno obj) throws SQLException {{}}
    public void delete(int id) throws SQLException {{}}
}
