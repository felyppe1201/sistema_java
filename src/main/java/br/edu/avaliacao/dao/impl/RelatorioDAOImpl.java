package br.edu.avaliacao.dao.impl;

import br.edu.avaliacao.dao.RelatorioDAO;
import br.edu.avaliacao.dtos.AlternativaEstatisticaDTO;
import br.edu.avaliacao.dtos.EstatisticaQuestaoDTO;
import br.edu.avaliacao.dtos.RelatorioTurmaDTO;

import java.util.Arrays;
import java.util.List;

public class RelatorioDAOImpl implements RelatorioDAO {

    @Override
    public List<RelatorioTurmaDTO> buscarRelatoriosConsolidadosPorProfessor(long idProfessor) {
        System.out.println("DAO: Buscando relatórios agregados para o Professor ID: " + idProfessor);

        List<AlternativaEstatisticaDTO> alt1 = Arrays.asList(
            new AlternativaEstatisticaDTO("Discordo Totalmente", 0.05, 1.0), 
            new AlternativaEstatisticaDTO("Discordo", 0.15, 2.0), 
            new AlternativaEstatisticaDTO("Concordo", 0.30, 3.0), 
            new AlternativaEstatisticaDTO("Concordo Totalmente", 0.50, 4.0) 
        );
        EstatisticaQuestaoDTO questao1 = new EstatisticaQuestaoDTO(
            "O professor demonstrou domínio do conteúdo da disciplina (Q. DOM01).", 
            alt1, 
            3.25, 
            "DOM01"
        );

        List<AlternativaEstatisticaDTO> alt2 = Arrays.asList(
            new AlternativaEstatisticaDTO("Sim", 0.92, 1.0),
            new AlternativaEstatisticaDTO("Não", 0.08, 0.0)
        );
        EstatisticaQuestaoDTO questao2 = new EstatisticaQuestaoDTO(
            "A metodologia de ensino foi adequada para a turma (Q. MET02).", 
            alt2, 
            0.92, 
            "MET02"
        );

        RelatorioTurmaDTO turma1 = new RelatorioTurmaDTO(
            "Programação Web I", 
            "WEB1234", 
            "Professor(a) de Exemplo", 
            0.85, 
            40,   
            34,   
            Arrays.asList(questao1, questao2)
        );

        RelatorioTurmaDTO turma2 = new RelatorioTurmaDTO(
            "Estruturas de Dados", 
            "ED0001", 
            "Professor(a) de Exemplo", 
            0.75, 
            30,
            22,
            Arrays.asList(questao1) 
        );

        return Arrays.asList(turma1, turma2);
    }
}