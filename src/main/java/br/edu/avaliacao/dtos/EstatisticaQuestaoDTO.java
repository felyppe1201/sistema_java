package br.edu.avaliacao.dtos;

import java.util.List;

public class EstatisticaQuestaoDTO {
    private String textoQuestao;
    private List<AlternativaEstatisticaDTO> alternativas;
    private double scoreTotal; 
    private String codigoQuestao; 

    public EstatisticaQuestaoDTO(String textoQuestao, List<AlternativaEstatisticaDTO> alternativas, double scoreTotal, String codigoQuestao) {
        this.textoQuestao = textoQuestao;
        this.alternativas = alternativas;
        this.scoreTotal = scoreTotal;
        this.codigoQuestao = codigoQuestao;
    }

    public String getTextoQuestao() { return textoQuestao; }
    public List<AlternativaEstatisticaDTO> getAlternativas() { return alternativas; }
    public double getScoreTotal() { return scoreTotal; }
    public String getCodigoQuestao() { return codigoQuestao; }
}