package com.dbserver.votacao.service;

import com.dbserver.votacao.client.ValidadorCpfClient;
import com.dbserver.votacao.dto.VotoRequestDTO;
import com.dbserver.votacao.enums.OpcaoVoto;
import com.dbserver.votacao.enums.StatusVotante;
import com.dbserver.votacao.exception.OperacaoInvalidaException;
import com.dbserver.votacao.model.Pauta;
import com.dbserver.votacao.model.SessaoVotacao;
import com.dbserver.votacao.repository.VotoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class VotoServiceTest {

    @Mock
    private VotoRepository votoRepository;

    @Mock
    private SessaoVotacaoService sessaoVotacaoService;

    @Mock
    private ValidadorCpfClient validadorCpfClient;

    @InjectMocks
    private VotoService votoService;

    private SessaoVotacao sessaoAberta;
    private SessaoVotacao sessaoEncerrada;

    @BeforeEach
    void configurarCenario() {
        Pauta pauta = new Pauta("Pauta X", "desc");
        pauta.setId(1L);

        sessaoAberta = new SessaoVotacao(pauta, LocalDateTime.now(), LocalDateTime.now().plusMinutes(5));
        sessaoAberta.setId(100L);

        sessaoEncerrada = new SessaoVotacao(pauta, LocalDateTime.now().minusMinutes(10), LocalDateTime.now().minusMinutes(5));
        sessaoEncerrada.setId(200L);
    }

    @Test
    void deveRegistrarVotoQuandoTudoEstiverOk() {
        when(sessaoVotacaoService.buscarEntidadePorPauta(1L)).thenReturn(sessaoAberta);
        when(votoRepository.existsBySessaoIdAndCpfAssociado(100L, "12345678900")).thenReturn(false);
        when(validadorCpfClient.consultarSituacaoDoAssociado("12345678900")).thenReturn(StatusVotante.ABLE_TO_VOTE);

        votoService.registrarVoto(1L, new VotoRequestDTO("12345678900", OpcaoVoto.SIM));

        verify(votoRepository).save(any());
    }

    @Test
    void naoDeveRegistrarVotoQuandoSessaoJaEncerrou() {
        when(sessaoVotacaoService.buscarEntidadePorPauta(1L)).thenReturn(sessaoEncerrada);

        assertThatThrownBy(() -> votoService.registrarVoto(1L, new VotoRequestDTO("12345678900", OpcaoVoto.SIM)))
                .isInstanceOf(OperacaoInvalidaException.class);

        verify(votoRepository, never()).save(any());
    }

    @Test
    void naoDeveRegistrarVotoQuandoAssociadoJaVotou() {
        when(sessaoVotacaoService.buscarEntidadePorPauta(1L)).thenReturn(sessaoAberta);
        when(votoRepository.existsBySessaoIdAndCpfAssociado(100L, "12345678900")).thenReturn(true);

        assertThatThrownBy(() -> votoService.registrarVoto(1L, new VotoRequestDTO("12345678900", OpcaoVoto.SIM)))
                .isInstanceOf(OperacaoInvalidaException.class);

        verify(votoRepository, never()).save(any());
    }

    @Test
    void naoDeveRegistrarVotoQuandoAssociadoNaoEstiverAptoAVotar() {
        when(sessaoVotacaoService.buscarEntidadePorPauta(1L)).thenReturn(sessaoAberta);
        when(votoRepository.existsBySessaoIdAndCpfAssociado(100L, "12345678900")).thenReturn(false);
        when(validadorCpfClient.consultarSituacaoDoAssociado("12345678900")).thenReturn(StatusVotante.UNABLE_TO_VOTE);

        assertThatThrownBy(() -> votoService.registrarVoto(1L, new VotoRequestDTO("12345678900", OpcaoVoto.SIM)))
                .isInstanceOf(OperacaoInvalidaException.class);

        verify(votoRepository, never()).save(any());
    }
}

