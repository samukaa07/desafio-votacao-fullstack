package com.dbserver.votacao.dto;

import com.dbserver.votacao.enums.StatusSessao;
import com.dbserver.votacao.model.SessaoVotacao;

import java.time.LocalDateTime;

/**
 * Retorno com os dados da sessao aberta, pra o front saber ate quando pode votar.
 */
public class SessaoVotacaoResponseDTO {

    private Long id;
    private Long pautaId;
    private LocalDateTime aberturaEm;
    private LocalDateTime encerramentoEm;
    private StatusSessao status;

    public SessaoVotacaoResponseDTO() {
    }

    public SessaoVotacaoResponseDTO(SessaoVotacao sessao) {
        this.id = sessao.getId();
        this.pautaId = sessao.getPauta().getId();
        this.aberturaEm = sessao.getAberturaEm();
        this.encerramentoEm = sessao.getEncerramentoEm();
        this.status = sessao.estaAberta() ? StatusSessao.ABERTA : StatusSessao.ENCERRADA;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPautaId() {
        return pautaId;
    }

    public void setPautaId(Long pautaId) {
        this.pautaId = pautaId;
    }

    public LocalDateTime getAberturaEm() {
        return aberturaEm;
    }

    public void setAberturaEm(LocalDateTime aberturaEm) {
        this.aberturaEm = aberturaEm;
    }

    public LocalDateTime getEncerramentoEm() {
        return encerramentoEm;
    }

    public void setEncerramentoEm(LocalDateTime encerramentoEm) {
        this.encerramentoEm = encerramentoEm;
    }

    public StatusSessao getStatus() {
        return status;
    }

    public void setStatus(StatusSessao status) {
        this.status = status;
    }
}

