package br.edu.avaliacao.dtos;

import java.util.List;

// DTO para as estatísticas consolidadas de uma Questão Fechada
public class EstatisticaQuestaoDTO {
    private String textoQuestao;
    private List<AlternativaEstatisticaDTO> alternativas;
    private double scoreTotal; // O Score calculado (RF17)
    private String codigoQuestao; 

    // Construtor completo
    public EstatisticaQuestaoDTO(String textoQuestao, List<AlternativaEstatisticaDTO> alternativas, double scoreTotal, String codigoQuestao) {
        this.textoQuestao = textoQuestao;
        this.alternativas = alternativas;
        this.scoreTotal = scoreTotal;
        this.codigoQuestao = codigoQuestao;
    }

    // Getters
    public String getTextoQuestao() { return textoQuestao; }
    public List<AlternativaEstatisticaDTO> getAlternativas() { return alternativas; }
    public double getScoreTotal() { return scoreTotal; }
    public String getCodigoQuestao() { return codigoQuestao; }
}