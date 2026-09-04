package com.dbserver.votacao.client;

import com.dbserver.votacao.enums.StatusVotante;
import com.dbserver.votacao.exception.CpfInvalidoException;
import org.springframework.stereotype.Component;

import java.util.Random;

/**
 * Client "fake" que simula uma integracao com um sistema externo de validacao de CPF.
 * Como pedido no desafio: sorteia se o CPF e valido e, sendo valido, sorteia se a pessoa
 * pode ou nao votar. Nada de chamada http de verdade aqui.
 */
@Component
public class ValidadorCpfClient {

    private final Random sorteio = new Random();

    /**
     * Consulta (fake) se o associado esta apto a votar.
     * Se o CPF "nao existir" (sorteado como invalido), lanca excecao que vira 404.
     */
    public StatusVotante consultarSituacaoDoAssociado(String cpf) {
        boolean cpfValido = sorteio.nextBoolean();

        if (!cpfValido) {
            throw new CpfInvalidoException("CPF " + cpf + " nao foi encontrado na base da receita (fake)");
        }

        return sorteio.nextBoolean() ? StatusVotante.ABLE_TO_VOTE : StatusVotante.UNABLE_TO_VOTE;
    }
}

