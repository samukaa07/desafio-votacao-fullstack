package com.dbserver.votacao.dto;

/**
 * Resultado consolidado da apuracao de uma pauta.
 */
public class ResultadoVotacaoDTO {

    private Long pautaId;
    private String tituloPauta;
    private long totalVotosSim;
    private long totalVotosNao;
    private String vencedor;

    public ResultadoVotacaoDTO() {
    }

    public ResultadoVotacaoDTO(Long pautaId, String tituloPauta, long totalVotosSim, long totalVotosNao) {
        this.pautaId = pautaId;
        this.tituloPauta = tituloPauta;
        this.totalVotosSim = totalVotosSim;
        this.totalVotosNao = totalVotosNao;
        this.vencedor = apurarVencedor(totalVotosSim, totalVotosNao);
    }

    private String apurarVencedor(long votosSim, long votosNao) {
        if (votosSim > votosNao) {
            return "SIM";
        }
        if (votosNao > votosSim) {
            return "NAO";
        }
        return "EMPATE";
    }

    public Long getPautaId() {
        return pautaId;
    }

    public void setPautaId(Long pautaId) {
        this.pautaId = pautaId;
    }

    public String getTituloPauta() {
        return tituloPauta;
    }

    public void setTituloPauta(String tituloPauta) {
        this.tituloPauta = tituloPauta;
    }

    public long getTotalVotosSim() {
        return totalVotosSim;
    }

    public void setTotalVotosSim(long totalVotosSim) {
        this.totalVotosSim = totalVotosSim;
    }

    public long getTotalVotosNao() {
        return totalVotosNao;
    }

    public void setTotalVotosNao(long totalVotosNao) {
        this.totalVotosNao = totalVotosNao;
    }

    public String getVencedor() {
        return vencedor;
    }

    public void setVencedor(String vencedor) {
        this.vencedor = vencedor;
    }
}

