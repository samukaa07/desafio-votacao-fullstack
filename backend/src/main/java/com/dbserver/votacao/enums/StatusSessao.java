package com.dbserver.votacao.enums;

/**
 * Status da sessao de votacao. Controlo o ciclo de vida da sessao por aqui:
 * comeca NAO_INICIADA, quando abre vira ABERTA, e quando o tempo estoura vira ENCERRADA.
 */
public enum StatusSessao {
    NAO_INICIADA,
    ABERTA,
    ENCERRADA
}

