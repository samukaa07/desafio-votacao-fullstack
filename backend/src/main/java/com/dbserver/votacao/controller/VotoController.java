package com.dbserver.votacao.controller;

import com.dbserver.votacao.dto.ResultadoVotacaoDTO;
import com.dbserver.votacao.dto.VotoRequestDTO;
import com.dbserver.votacao.service.ResultadoVotacaoService;
import com.dbserver.votacao.service.VotoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Endpoints de votacao propriamente dita e apuracao do resultado.
 */
@RestController
@RequestMapping("/api/v1/pautas/{pautaId}")
public class VotoController {

    private final VotoService votoService;
    private final ResultadoVotacaoService resultadoVotacaoService;

    public VotoController(VotoService votoService, ResultadoVotacaoService resultadoVotacaoService) {
        this.votoService = votoService;
        this.resultadoVotacaoService = resultadoVotacaoService;
    }

    @PostMapping("/votos")
    public ResponseEntity<Void> votar(@PathVariable Long pautaId, @Valid @RequestBody VotoRequestDTO dados) {
        votoService.registrarVoto(pautaId, dados);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/resultado")
    public ResponseEntity<ResultadoVotacaoDTO> apurarResultado(@PathVariable Long pautaId) {
        return ResponseEntity.ok(resultadoVotacaoService.apurar(pautaId));
    }
}

