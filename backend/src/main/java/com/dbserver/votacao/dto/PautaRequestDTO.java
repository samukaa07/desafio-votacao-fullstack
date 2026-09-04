package com.dbserver.votacao.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Dados recebidos para cadastrar uma pauta nova.
 */
public class PautaRequestDTO {

    @NotBlank(message = "titulo e obrigatorio")
    @Size(max = 150, message = "titulo deve ter no maximo 150 caracteres")
    private String titulo;

    @Size(max = 1000, message = "descricao deve ter no maximo 1000 caracteres")
    private String descricao;

    public PautaRequestDTO() {
    }

    public PautaRequestDTO(String titulo, String descricao) {
        this.titulo = titulo;
        this.descricao = descricao;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
}

