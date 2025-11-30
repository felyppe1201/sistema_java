package br.edu.avaliacao.dtos;

// DTO para as estatísticas de cada alternativa de uma questão
// Contém o percentual de escolha e o peso para o cálculo do Score.
public class AlternativaEstatisticaDTO {
    private String textoAlternativa;
    private double percentualResposta; // Ex: 0.60 para 60%
    private double pesoAtribuido;

    // Construtor completo
    public AlternativaEstatisticaDTO(String textoAlternativa, double percentualResposta, double pesoAtribuido) {
        this.textoAlternativa = textoAlternativa;
        this.percentualResposta = percentualResposta;
        this.pesoAtribuido = pesoAtribuido;
    }

    // Getters
    public String getTextoAlternativa() { return textoAlternativa; }
    public double getPercentualResposta() { return percentualResposta; }
    public double getPesoAtribuido() { return pesoAtribuido; }
}