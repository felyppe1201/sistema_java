package br.edu.avaliacao.dtos;

public class AlternativaEstatisticaDTO {
    private String textoAlternativa;
    private double percentualResposta; 
    private double pesoAtribuido;

    public AlternativaEstatisticaDTO(String textoAlternativa, double percentualResposta, double pesoAtribuido) {
        this.textoAlternativa = textoAlternativa;
        this.percentualResposta = percentualResposta;
        this.pesoAtribuido = pesoAtribuido;
    }

    public String getTextoAlternativa() { return textoAlternativa; }
    public double getPercentualResposta() { return percentualResposta; }
    public double getPesoAtribuido() { return pesoAtribuido; }
}