package com.dbserver.votacao.exception;

/**
 * Disparada quando o cliente pede pra mexer em algo que nao existe (pauta, sessao, etc).
 */
public class RecursoNaoEncontradoException extends RuntimeException {

    public RecursoNaoEncontradoException(String mensagem) {
        super(mensagem);
    }
}

