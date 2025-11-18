
package br.edu.avaliacao.repository;

import br.edu.avaliacao.models.ProcessoAvaliativo;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class ProcessoAvaliativoRepository {
    protected Connection conn;
    public ProcessoAvaliativoRepository(Connection conn){{ this.conn = conn; }}

    public void save(ProcessoAvaliativo obj) throws SQLException {{}}
    public ProcessoAvaliativo findById(int id) throws SQLException {{ return null; }}
    public List<ProcessoAvaliativo> findAll() throws SQLException {{ return null; }}
    public void update(ProcessoAvaliativo obj) throws SQLException {{}}
    public void delete(int id) throws SQLException {{}}
}
