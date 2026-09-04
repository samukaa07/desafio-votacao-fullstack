package com.dbserver.votacao.model;

import com.dbserver.votacao.enums.StatusSessao;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

/**
 * Representa a janela de tempo em que uma pauta pode receber votos.
 * Cada pauta so pode ter uma sessao (nesse desafio simples nao ha reabertura).
 */
@Entity
@Table(name = "sessao_votacao")
public class SessaoVotacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pauta_id", nullable = false, unique = true)
    private Pauta pauta;

    @Column(name = "abertura_em", nullable = false)
    private LocalDateTime aberturaEm;

    @Column(name = "encerramento_em", nullable = false)
    private LocalDateTime encerramentoEm;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private StatusSessao status;

    public SessaoVotacao() {
    }

    public SessaoVotacao(Pauta pauta, LocalDateTime aberturaEm, LocalDateTime encerramentoEm) {
        this.pauta = pauta;
        this.aberturaEm = aberturaEm;
        this.encerramentoEm = encerramentoEm;
        this.status = StatusSessao.ABERTA;
    }

    /**
     * Verifica se, na hora atual, a sessao ainda aceita votos.
     */
    public boolean estaAberta() {
        return LocalDateTime.now().isBefore(this.encerramentoEm);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Pauta getPauta() {
        return pauta;
    }

    public void setPauta(Pauta pauta) {
        this.pauta = pauta;
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

