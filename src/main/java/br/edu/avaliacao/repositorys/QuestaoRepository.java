
package br.edu.avaliacao.repositorys;

import br.edu.avaliacao.models.Questao;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class QuestaoRepository {
    protected Connection conn;
    public QuestaoRepository(Connection conn){ this.conn = conn; }

    public void save(Questao obj) throws SQLException {}
    public Questao findById(long id) throws SQLException { return null; }
    public List<Questao> findAll() throws SQLException { return null; }
    public void update(Questao obj) throws SQLException {}
    public void delete(long id) throws SQLException {}
}
