package com.dbserver.votacao.exception;

/**
 * Disparada pelo client fake de CPF quando o CPF informado nao e valido.
 * Vira um 404 pra quem chamou a API.
 */
public class CpfInvalidoException extends RuntimeException {

    public CpfInvalidoException(String mensagem) {
        super(mensagem);
    }
}

