package com.dbserver.votacao.model;

import com.dbserver.votacao.enums.OpcaoVoto;
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
import jakarta.persistence.UniqueConstraint;

import java.time.LocalDateTime;

/**
 * Um voto de um associado em uma sessao. O par (sessao, associado) e unico -
 * ninguem vota duas vezes na mesma pauta.
 */
@Entity
@Table(name = "voto", uniqueConstraints = @UniqueConstraint(columnNames = {"sessao_id", "cpf_associado"}))
public class Voto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sessao_id", nullable = false)
    private SessaoVotacao sessao;

    @Column(name = "cpf_associado", nullable = false, length = 11)
    private String cpfAssociado;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 3)
    private OpcaoVoto opcao;

    @Column(name = "votado_em", nullable = false)
    private LocalDateTime votadoEm;

    public Voto() {
    }

    public Voto(SessaoVotacao sessao, String cpfAssociado, OpcaoVoto opcao) {
        this.sessao = sessao;
        this.cpfAssociado = cpfAssociado;
        this.opcao = opcao;
        this.votadoEm = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public SessaoVotacao getSessao() {
        return sessao;
    }

    public void setSessao(SessaoVotacao sessao) {
        this.sessao = sessao;
    }

    public String getCpfAssociado() {
        return cpfAssociado;
    }

    public void setCpfAssociado(String cpfAssociado) {
        this.cpfAssociado = cpfAssociado;
    }

    public OpcaoVoto getOpcao() {
        return opcao;
    }

    public void setOpcao(OpcaoVoto opcao) {
        this.opcao = opcao;
    }

    public LocalDateTime getVotadoEm() {
        return votadoEm;
    }

    public void setVotadoEm(LocalDateTime votadoEm) {
        this.votadoEm = votadoEm;
    }
}

