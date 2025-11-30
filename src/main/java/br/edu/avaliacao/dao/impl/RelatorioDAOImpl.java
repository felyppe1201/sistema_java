package br.edu.avaliacao.dao.impl;

import br.edu.avaliacao.dao.RelatorioDAO;
import br.edu.avaliacao.dtos.AlternativaEstatisticaDTO;
import br.edu.avaliacao.dtos.EstatisticaQuestaoDTO;
import br.edu.avaliacao.dtos.RelatorioTurmaDTO;

import java.util.Arrays;
import java.util.List;

// SIMULAÇÃO: No projeto final, esta classe implementaria a lógica de consulta SQL (com COUNT, GROUP BY) ou HQL/Criteria API (JPA)
// para calcular as estatísticas diretamente no banco, sem carregar respostas individuais.
public class RelatorioDAOImpl implements RelatorioDAO {
    
    // Simula a busca no banco, garantindo que os dados retornados são AGREGADOS
    @Override
    public List<RelatorioTurmaDTO> buscarRelatoriosConsolidadosPorProfessor(long idProfessor) {
        System.out.println("DAO: Buscando relatórios agregados para o Professor ID: " + idProfessor);

        // --- Simulação de Estatísticas (Questão 1) ---
        List<AlternativaEstatisticaDTO> alt1 = Arrays.asList(
            new AlternativaEstatisticaDTO("Discordo Totalmente", 0.05, 1.0), 
            new AlternativaEstatisticaDTO("Discordo", 0.15, 2.0), 
            new AlternativaEstatisticaDTO("Concordo", 0.30, 3.0), 
            new AlternativaEstatisticaDTO("Concordo Totalmente", 0.50, 4.0) 
        );
        // Score: 3.25
        EstatisticaQuestaoDTO questao1 = new EstatisticaQuestaoDTO(
            "O professor demonstrou domínio do conteúdo da disciplina (Q. DOM01).", 
            alt1, 
            3.25, 
            "DOM01"
        );
        
        // --- Simulação de Estatísticas (Questão 2) ---
        List<AlternativaEstatisticaDTO> alt2 = Arrays.asList(
            new AlternativaEstatisticaDTO("Sim", 0.92, 1.0),
            new AlternativaEstatisticaDTO("Não", 0.08, 0.0)
        );
        // Score: 0.92
        EstatisticaQuestaoDTO questao2 = new EstatisticaQuestaoDTO(
            "A metodologia de ensino foi adequada para a turma (Q. MET02).", 
            alt2, 
            0.92, 
            "MET02"
        );

        // --- Simulação do Relatório da Turma 1 ---
        RelatorioTurmaDTO turma1 = new RelatorioTurmaDTO(
            "Programação Web I", 
            "WEB1234", 
            "Professor(a) de Exemplo", 
            0.85, 
            40,   
            34,   
            Arrays.asList(questao1, questao2)
        );

        // --- Simulação do Relatório da Turma 2 ---
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