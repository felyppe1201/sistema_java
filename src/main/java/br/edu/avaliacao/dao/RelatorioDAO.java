package br.edu.avaliacao.dao;

import br.edu.avaliacao.dtos.RelatorioTurmaDTO;
import java.util.List;

// Define os métodos para a busca de dados agregados no banco de dados (JDBC/JPA Repository)
public interface RelatorioDAO {
    
    /**
     * Busca relatórios consolidados (AGREGADOS) para todas as turmas que um professor leciona.
     * Esta camada é crucial para garantir que apenas dados estatísticos sejam buscados,
     * protegendo o anonimato das respostas individuais (RF19).
     *
     * @param idProfessor O ID do usuário logado (professor).
     * @return Uma lista de DTOs contendo as estatísticas de cada turma.
     */
    List<RelatorioTurmaDTO> buscarRelatoriosConsolidadosPorProfessor(long idProfessor);
    
    // NOTA: Se estivesse usando Spring Data JPA, isso seria um @Repository.
}