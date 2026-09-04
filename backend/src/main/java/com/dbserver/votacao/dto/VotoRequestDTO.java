package com.dbserver.votacao.dto;

import com.dbserver.votacao.enums.OpcaoVoto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

/**
 * Dados de um voto: quem esta votando (CPF) e a escolha (SIM/NAO).
 */
public class VotoRequestDTO {

    @NotBlank(message = "cpfAssociado e obrigatorio")
    @Pattern(regexp = "\\d{11}", message = "cpfAssociado deve conter 11 digitos numericos")
    private String cpfAssociado;

    @NotNull(message = "opcao e obrigatoria")
    private OpcaoVoto opcao;

    public VotoRequestDTO() {
    }

    public VotoRequestDTO(String cpfAssociado, OpcaoVoto opcao) {
        this.cpfAssociado = cpfAssociado;
        this.opcao = opcao;
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
}

