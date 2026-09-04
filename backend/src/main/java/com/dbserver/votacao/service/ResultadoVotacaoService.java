package com.dbserver.votacao.service;

import com.dbserver.votacao.dto.ResultadoVotacaoDTO;
import com.dbserver.votacao.enums.OpcaoVoto;
import com.dbserver.votacao.model.Pauta;
import com.dbserver.votacao.model.SessaoVotacao;
import com.dbserver.votacao.repository.VotoRepository;
import org.springframework.stereotype.Service;

/**
 * Apura o resultado de uma pauta contando os votos SIM/NAO ja registrados.
 */
@Service
public class ResultadoVotacaoService {

    private final VotoRepository votoRepository;
    private final SessaoVotacaoService sessaoVotacaoService;
    private final PautaService pautaService;

    public ResultadoVotacaoService(VotoRepository votoRepository,
                                    SessaoVotacaoService sessaoVotacaoService,
                                    PautaService pautaService) {
        this.votoRepository = votoRepository;
        this.sessaoVotacaoService = sessaoVotacaoService;
        this.pautaService = pautaService;
    }

    public ResultadoVotacaoDTO apurar(Long pautaId) {
        Pauta pauta = pautaService.buscarEntidadePorId(pautaId);
        SessaoVotacao sessao = sessaoVotacaoService.buscarEntidadePorPauta(pautaId);

        long votosSim = votoRepository.countBySessaoIdAndOpcao(sessao.getId(), OpcaoVoto.SIM);
        long votosNao = votoRepository.countBySessaoIdAndOpcao(sessao.getId(), OpcaoVoto.NAO);

        return new ResultadoVotacaoDTO(pauta.getId(), pauta.getTitulo(), votosSim, votosNao);
    }
}

