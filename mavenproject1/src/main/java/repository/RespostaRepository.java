
package repository;

import models.Resposta;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class RespostaRepository {
    protected Connection conn;
    public RespostaRepository(Connection conn){{ this.conn = conn; }}

    public void save(Resposta obj) throws SQLException {{}}
    public Resposta findById(int id) throws SQLException {{ return null; }}
    public List<Resposta> findAll() throws SQLException {{ return null; }}
    public void update(Resposta obj) throws SQLException {{}}
    public void delete(int id) throws SQLException {{}}
}
