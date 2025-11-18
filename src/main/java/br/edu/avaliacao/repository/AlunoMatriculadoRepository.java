
package br.edu.avaliacao.repository;

import br.edu.avaliacao.models.AlunoMatriculado;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class AlunoMatriculadoRepository {
    protected Connection conn;
    public AlunoMatriculadoRepository(Connection conn){ this.conn = conn; }

    public void save(AlunoMatriculado obj) throws SQLException {}
    public AlunoMatriculado findById(long id) throws SQLException { return null; }
    public List<AlunoMatriculado> findAll() throws SQLException { return null; }
    public void update(AlunoMatriculado obj) throws SQLException {}
    public void delete(long id) throws SQLException {}
}
