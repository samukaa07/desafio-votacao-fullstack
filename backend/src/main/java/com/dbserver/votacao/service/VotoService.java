package com.dbserver.votacao.service;

import com.dbserver.votacao.client.ValidadorCpfClient;
import com.dbserver.votacao.dto.VotoRequestDTO;
import com.dbserver.votacao.enums.OpcaoVoto;
import com.dbserver.votacao.enums.StatusVotante;
import com.dbserver.votacao.exception.OperacaoInvalidaException;
import com.dbserver.votacao.model.SessaoVotacao;
import com.dbserver.votacao.model.Voto;
import com.dbserver.votacao.repository.VotoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Recebe e registra os votos dos associados, respeitando as regras:
 * sessao aberta, associado apto a votar e voto unico por pauta.
 */
@Service
public class VotoService {

    private static final Logger log = LoggerFactory.getLogger(VotoService.class);

    private final VotoRepository votoRepository;
    private final SessaoVotacaoService sessaoVotacaoService;
    private final ValidadorCpfClient validadorCpfClient;

    public VotoService(VotoRepository votoRepository,
                        SessaoVotacaoService sessaoVotacaoService,
                        ValidadorCpfClient validadorCpfClient) {
        this.votoRepository = votoRepository;
        this.sessaoVotacaoService = sessaoVotacaoService;
        this.validadorCpfClient = validadorCpfClient;
    }

    public void registrarVoto(Long pautaId, VotoRequestDTO dados) {
        SessaoVotacao sessao = sessaoVotacaoService.buscarEntidadePorPauta(pautaId);

        if (!sessao.estaAberta()) {
            throw new OperacaoInvalidaException("A sessao de votacao da pauta " + pautaId + " ja foi encerrada");
        }

        boolean jaVotou = votoRepository.existsBySessaoIdAndCpfAssociado(sessao.getId(), dados.getCpfAssociado());
        if (jaVotou) {
            throw new OperacaoInvalidaException("O associado " + dados.getCpfAssociado() + " ja votou nessa pauta");
        }

        StatusVotante situacao = validadorCpfClient.consultarSituacaoDoAssociado(dados.getCpfAssociado());
        if (situacao == StatusVotante.UNABLE_TO_VOTE) {
            throw new OperacaoInvalidaException("Associado nao esta apto a votar no momento");
        }

        OpcaoVoto opcao = dados.getOpcao();
        Voto voto = new Voto(sessao, dados.getCpfAssociado(), opcao);
        votoRepository.save(voto);

        log.info("Voto registrado. pauta={} cpf={} opcao={}", pautaId, dados.getCpfAssociado(), opcao);
    }
}

