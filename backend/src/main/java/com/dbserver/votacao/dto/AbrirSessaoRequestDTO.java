package com.dbserver.votacao.dto;

/**
 * Duracao em minutos que a sessao deve ficar aberta. Se vier nulo, uso o valor padrao (1 minuto).
 */
public class AbrirSessaoRequestDTO {

    private Integer duracaoEmMinutos;

    public AbrirSessaoRequestDTO() {
    }

    public AbrirSessaoRequestDTO(Integer duracaoEmMinutos) {
        this.duracaoEmMinutos = duracaoEmMinutos;
    }

    public Integer getDuracaoEmMinutos() {
        return duracaoEmMinutos;
    }

    public void setDuracaoEmMinutos(Integer duracaoEmMinutos) {
        this.duracaoEmMinutos = duracaoEmMinutos;
    }
}

