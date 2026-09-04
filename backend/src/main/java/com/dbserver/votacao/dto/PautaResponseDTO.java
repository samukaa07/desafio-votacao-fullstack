package com.dbserver.votacao.dto;

import com.dbserver.votacao.model.Pauta;

import java.time.LocalDateTime;

/**
 * O que devolvemos para o front quando falamos de uma pauta.
 */
public class PautaResponseDTO {

    private Long id;
    private String titulo;
    private String descricao;
    private LocalDateTime criadaEm;

    public PautaResponseDTO() {
    }

    public PautaResponseDTO(Pauta pauta) {
        this.id = pauta.getId();
        this.titulo = pauta.getTitulo();
        this.descricao = pauta.getDescricao();
        this.criadaEm = pauta.getCriadaEm();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public LocalDateTime getCriadaEm() {
        return criadaEm;
    }

    public void setCriadaEm(LocalDateTime criadaEm) {
        this.criadaEm = criadaEm;
    }
}

