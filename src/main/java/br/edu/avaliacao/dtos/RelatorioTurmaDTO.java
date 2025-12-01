package br.edu.avaliacao.dtos;

import java.util.List;

// DTO que consolida os resultados do relatório para uma Turma específica.
public class RelatorioTurmaDTO {
    private String nomeDisciplina;
    private String codigoTurma;
    private String nomeProfessor;
    private double taxaConclusao; // Ex: 0.85 para 85%
    private int totalAlunosMatriculados;
    private int totalRespostasColetadas;
    private List<EstatisticaQuestaoDTO> estatisticasQuestoes; // Estatísticas das questões fechadas

    // Construtor completo
    public RelatorioTurmaDTO(String nomeDisciplina, String codigoTurma, String nomeProfessor, double taxaConclusao, int totalAlunosMatriculados, int totalRespostasColetadas, List<EstatisticaQuestaoDTO> estatisticasQuestoes) {
        this.nomeDisciplina = nomeDisciplina;
        this.codigoTurma = codigoTurma;
        this.nomeProfessor = nomeProfessor;
        this.taxaConclusao = taxaConclusao;
        this.totalAlunosMatriculados = totalAlunosMatriculados;
        this.totalRespostasColetadas = totalRespostasColetadas;
        this.estatisticasQuestoes = estatisticasQuestoes;
    }

    // Getters
    public String getNomeDisciplina() { return nomeDisciplina; }
    public String getCodigoTurma() { return codigoTurma; }
    public String getNomeProfessor() { return nomeProfessor; }
    public double getTaxaConclusao() { return taxaConclusao; }
    public int getTotalAlunosMatriculados() { return totalAlunosMatriculados; }
    public int getTotalRespostasColetadas() { return totalRespostasColetadas; }
    public List<EstatisticaQuestaoDTO> getEstatisticasQuestoes() { return estatisticasQuestoes; }
}