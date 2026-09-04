package com.dbserver.votacao.service;

import com.dbserver.votacao.dto.ResultadoVotacaoDTO;
import com.dbserver.votacao.enums.OpcaoVoto;
import com.dbserver.votacao.model.Pauta;
import com.dbserver.votacao.model.SessaoVotacao;
import com.dbserver.votacao.repository.VotoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ResultadoVotacaoServiceTest {

    @Mock
    private VotoRepository votoRepository;

    @Mock
    private SessaoVotacaoService sessaoVotacaoService;

    @Mock
    private PautaService pautaService;

    @InjectMocks
    private ResultadoVotacaoService resultadoVotacaoService;

    @Test
    void deveApurarVencedorComoSimQuandoHaMaisVotosSim() {
        Pauta pauta = new Pauta("Pauta", "desc");
        pauta.setId(1L);
        SessaoVotacao sessao = new SessaoVotacao(pauta, LocalDateTime.now(), LocalDateTime.now().plusMinutes(1));
        sessao.setId(50L);

        when(pautaService.buscarEntidadePorId(1L)).thenReturn(pauta);
        when(sessaoVotacaoService.buscarEntidadePorPauta(1L)).thenReturn(sessao);
        when(votoRepository.countBySessaoIdAndOpcao(50L, OpcaoVoto.SIM)).thenReturn(7L);
        when(votoRepository.countBySessaoIdAndOpcao(50L, OpcaoVoto.NAO)).thenReturn(3L);

        ResultadoVotacaoDTO resultado = resultadoVotacaoService.apurar(1L);

        assertThat(resultado.getTotalVotosSim()).isEqualTo(7L);
        assertThat(resultado.getTotalVotosNao()).isEqualTo(3L);
        assertThat(resultado.getVencedor()).isEqualTo("SIM");
    }

    @Test
    void deveApurarEmpateQuandoVotosForemIguais() {
        Pauta pauta = new Pauta("Pauta", "desc");
        pauta.setId(2L);
        SessaoVotacao sessao = new SessaoVotacao(pauta, LocalDateTime.now(), LocalDateTime.now().plusMinutes(1));
        sessao.setId(51L);

        when(pautaService.buscarEntidadePorId(2L)).thenReturn(pauta);
        when(sessaoVotacaoService.buscarEntidadePorPauta(2L)).thenReturn(sessao);
        when(votoRepository.countBySessaoIdAndOpcao(51L, OpcaoVoto.SIM)).thenReturn(4L);
        when(votoRepository.countBySessaoIdAndOpcao(51L, OpcaoVoto.NAO)).thenReturn(4L);

        ResultadoVotacaoDTO resultado = resultadoVotacaoService.apurar(2L);

        assertThat(resultado.getVencedor()).isEqualTo("EMPATE");
    }
}

