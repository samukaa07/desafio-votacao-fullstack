package com.dbserver.votacao.service;

import com.dbserver.votacao.dto.AbrirSessaoRequestDTO;
import com.dbserver.votacao.dto.SessaoVotacaoResponseDTO;
import com.dbserver.votacao.exception.OperacaoInvalidaException;
import com.dbserver.votacao.exception.RecursoNaoEncontradoException;
import com.dbserver.votacao.model.Pauta;
import com.dbserver.votacao.model.SessaoVotacao;
import com.dbserver.votacao.repository.SessaoVotacaoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * Cuida da abertura e consulta das sessoes de votacao de cada pauta.
 */
@Service
public class SessaoVotacaoService {

    private static final Logger log = LoggerFactory.getLogger(SessaoVotacaoService.class);

    private final SessaoVotacaoRepository sessaoVotacaoRepository;
    private final PautaService pautaService;
    private final int duracaoPadraoMinutos;

    public SessaoVotacaoService(SessaoVotacaoRepository sessaoVotacaoRepository,
                                 PautaService pautaService,
                                 @Value("${votacao.sessao.duracao-padrao-minutos:1}") int duracaoPadraoMinutos) {
        this.sessaoVotacaoRepository = sessaoVotacaoRepository;
        this.pautaService = pautaService;
        this.duracaoPadraoMinutos = duracaoPadraoMinutos;
    }

    public SessaoVotacaoResponseDTO abrir(Long pautaId, AbrirSessaoRequestDTO dados) {
        Pauta pauta = pautaService.buscarEntidadePorId(pautaId);

        sessaoVotacaoRepository.findByPautaId(pautaId).ifPresent(sessaoExistente -> {
            throw new OperacaoInvalidaException("A pauta " + pautaId + " ja possui uma sessao de votacao");
        });

        int minutos = (dados != null && dados.getDuracaoEmMinutos() != null && dados.getDuracaoEmMinutos() > 0)
                ? dados.getDuracaoEmMinutos()
                : duracaoPadraoMinutos;

        LocalDateTime agora = LocalDateTime.now();
        SessaoVotacao sessao = new SessaoVotacao(pauta, agora, agora.plusMinutes(minutos));
        sessao = sessaoVotacaoRepository.save(sessao);

        log.info("Sessao aberta para pauta={} duracao={}min encerra_em={}", pautaId, minutos, sessao.getEncerramentoEm());
        return new SessaoVotacaoResponseDTO(sessao);
    }

    public SessaoVotacaoResponseDTO buscarPorPauta(Long pautaId) {
        return new SessaoVotacaoResponseDTO(buscarEntidadePorPauta(pautaId));
    }

    public SessaoVotacao buscarEntidadePorPauta(Long pautaId) {
        return sessaoVotacaoRepository.findByPautaId(pautaId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Nao existe sessao de votacao para a pauta " + pautaId));
    }
}

