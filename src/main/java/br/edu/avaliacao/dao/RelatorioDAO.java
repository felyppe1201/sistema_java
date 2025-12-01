package br.edu.avaliacao.dao;

import br.edu.avaliacao.dtos.RelatorioTurmaDTO;
import java.util.List;

public interface RelatorioDAO {
    List<RelatorioTurmaDTO> buscarRelatoriosConsolidadosPorProfessor(long idProfessor);
}