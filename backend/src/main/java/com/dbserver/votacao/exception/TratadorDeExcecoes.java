package com.dbserver.votacao.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Handler central de excecoes. Prefiro concentrar aqui a traducao de excecao -> resposta HTTP
 * do que ficar espalhando try/catch pelos controllers.
 */
@RestControllerAdvice
public class TratadorDeExcecoes {

    @ExceptionHandler(RecursoNaoEncontradoException.class)
    public ResponseEntity<Object> tratarRecursoNaoEncontrado(RecursoNaoEncontradoException ex) {
        return montarResposta(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(CpfInvalidoException.class)
    public ResponseEntity<Object> tratarCpfInvalido(CpfInvalidoException ex) {
        return montarResposta(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(OperacaoInvalidaException.class)
    public ResponseEntity<Object> tratarOperacaoInvalida(OperacaoInvalidaException ex) {
        return montarResposta(HttpStatus.UNPROCESSABLE_ENTITY, ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> tratarErrosDeValidacao(MethodArgumentNotValidException ex) {
        String mensagens = ex.getBindingResult().getFieldErrors().stream()
                .map(erro -> erro.getField() + ": " + erro.getDefaultMessage())
                .reduce((a, b) -> a + "; " + b)
                .orElse("Dados invalidos");
        return montarResposta(HttpStatus.BAD_REQUEST, mensagens);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> tratarErroGenerico(Exception ex) {
        return montarResposta(HttpStatus.INTERNAL_SERVER_ERROR, "Ocorreu um erro inesperado: " + ex.getMessage());
    }

    private ResponseEntity<Object> montarResposta(HttpStatus status, String mensagem) {
        Map<String, Object> corpo = new LinkedHashMap<>();
        corpo.put("timestamp", LocalDateTime.now());
        corpo.put("status", status.value());
        corpo.put("erro", status.getReasonPhrase());
        corpo.put("mensagem", mensagem);
        return ResponseEntity.status(status).body(corpo);
    }
}

