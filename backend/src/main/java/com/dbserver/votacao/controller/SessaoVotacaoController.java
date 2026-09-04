package com.dbserver.votacao.controller;

import com.dbserver.votacao.dto.AbrirSessaoRequestDTO;
import com.dbserver.votacao.dto.SessaoVotacaoResponseDTO;
import com.dbserver.votacao.service.SessaoVotacaoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Endpoints para abrir e consultar a sessao de votacao de uma pauta.
 */
@RestController
@RequestMapping("/api/v1/pautas/{pautaId}/sessao")
public class SessaoVotacaoController {

    private final SessaoVotacaoService sessaoVotacaoService;

    public SessaoVotacaoController(SessaoVotacaoService sessaoVotacaoService) {
        this.sessaoVotacaoService = sessaoVotacaoService;
    }

    @PostMapping
    public ResponseEntity<SessaoVotacaoResponseDTO> abrir(@PathVariable Long pautaId,
                                                            @RequestBody(required = false) AbrirSessaoRequestDTO dados) {
        SessaoVotacaoResponseDTO sessao = sessaoVotacaoService.abrir(pautaId, dados);
        return ResponseEntity.status(HttpStatus.CREATED).body(sessao);
    }

    @GetMapping
    public ResponseEntity<SessaoVotacaoResponseDTO> buscar(@PathVariable Long pautaId) {
        return ResponseEntity.ok(sessaoVotacaoService.buscarPorPauta(pautaId));
    }
}

