
package br.edu.avaliacao.repository;

import br.edu.avaliacao.models.AtribuicaoProfessor;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class AtribuicaoProfessorRepository {
    protected Connection conn;
    public AtribuicaoProfessorRepository(Connection conn){{ this.conn = conn; }}

    public void save(AtribuicaoProfessor obj) throws SQLException {{}}
    public AtribuicaoProfessor findById(int id) throws SQLException {{ return null; }}
    public List<AtribuicaoProfessor> findAll() throws SQLException {{ return null; }}
    public void update(AtribuicaoProfessor obj) throws SQLException {{}}
    public void delete(int id) throws SQLException {{}}
}
