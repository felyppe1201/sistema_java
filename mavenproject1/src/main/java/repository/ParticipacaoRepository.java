
package repository;

import models.Participacao;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class ParticipacaoRepository {
    protected Connection conn;
    public ParticipacaoRepository(Connection conn){{ this.conn = conn; }}

    public void save(Participacao obj) throws SQLException {{}}
    public Participacao findById(int id) throws SQLException {{ return null; }}
    public List<Participacao> findAll() throws SQLException {{ return null; }}
    public void update(Participacao obj) throws SQLException {{}}
    public void delete(int id) throws SQLException {{}}
}
