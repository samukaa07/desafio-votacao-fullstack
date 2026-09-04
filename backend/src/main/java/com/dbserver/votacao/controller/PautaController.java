package com.dbserver.votacao.controller;

import com.dbserver.votacao.dto.PautaRequestDTO;
import com.dbserver.votacao.dto.PautaResponseDTO;
import com.dbserver.votacao.service.PautaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Endpoints para cadastro e consulta das pautas da assembleia.
 */
@RestController
@RequestMapping("/api/v1/pautas")
public class PautaController {

    private final PautaService pautaService;

    public PautaController(PautaService pautaService) {
        this.pautaService = pautaService;
    }

    @PostMapping
    public ResponseEntity<PautaResponseDTO> cadastrar(@Valid @RequestBody PautaRequestDTO dados) {
        PautaResponseDTO pautaCriada = pautaService.cadastrar(dados);
        return ResponseEntity.status(HttpStatus.CREATED).body(pautaCriada);
    }

    @GetMapping
    public ResponseEntity<List<PautaResponseDTO>> listarTodas() {
        return ResponseEntity.ok(pautaService.listarTodas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PautaResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(pautaService.buscarPorId(id));
    }
}

