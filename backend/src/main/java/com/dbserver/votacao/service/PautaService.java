package com.dbserver.votacao.service;

import com.dbserver.votacao.dto.PautaRequestDTO;
import com.dbserver.votacao.dto.PautaResponseDTO;
import com.dbserver.votacao.exception.RecursoNaoEncontradoException;
import com.dbserver.votacao.model.Pauta;
import com.dbserver.votacao.repository.PautaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Regras de negocio ligadas a cadastro e consulta de pautas.
 */
@Service
public class PautaService {

    private static final Logger log = LoggerFactory.getLogger(PautaService.class);

    private final PautaRepository pautaRepository;

    public PautaService(PautaRepository pautaRepository) {
        this.pautaRepository = pautaRepository;
    }

    public PautaResponseDTO cadastrar(PautaRequestDTO dados) {
        Pauta pauta = new Pauta(dados.getTitulo(), dados.getDescricao());
        pauta = pautaRepository.save(pauta);
        log.info("Pauta cadastrada. id={}, titulo={}", pauta.getId(), pauta.getTitulo());
        return new PautaResponseDTO(pauta);
    }

    public List<PautaResponseDTO> listarTodas() {
        return pautaRepository.findAll().stream()
                .map(PautaResponseDTO::new)
                .collect(Collectors.toList());
    }

    public PautaResponseDTO buscarPorId(Long id) {
        return new PautaResponseDTO(buscarEntidadePorId(id));
    }

    public Pauta buscarEntidadePorId(Long id) {
        return pautaRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Pauta " + id + " nao encontrada"));
    }
}

