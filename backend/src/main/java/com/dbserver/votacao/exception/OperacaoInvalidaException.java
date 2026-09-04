package com.dbserver.votacao.exception;

/**
 * Disparada quando o estado atual nao permite a operacao pedida
 * (ex: abrir sessao que ja esta aberta, votar em sessao encerrada, votar duas vezes...).
 */
public class OperacaoInvalidaException extends RuntimeException {

    public OperacaoInvalidaException(String mensagem) {
        super(mensagem);
    }
}

