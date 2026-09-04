package com.dbserver.votacao.service;

import com.dbserver.votacao.dto.AbrirSessaoRequestDTO;
import com.dbserver.votacao.dto.SessaoVotacaoResponseDTO;
import com.dbserver.votacao.exception.OperacaoInvalidaException;
import com.dbserver.votacao.model.Pauta;
import com.dbserver.votacao.model.SessaoVotacao;
import com.dbserver.votacao.repository.SessaoVotacaoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SessaoVotacaoServiceTest {

    @Mock
    private SessaoVotacaoRepository sessaoVotacaoRepository;

    @Mock
    private PautaService pautaService;

    private SessaoVotacaoService sessaoVotacaoService;

    private Pauta pauta;

    @BeforeEach
    void configurarCenario() {
        sessaoVotacaoService = new SessaoVotacaoService(sessaoVotacaoRepository, pautaService, 1);
        pauta = new Pauta("Pauta de teste", "descricao");
        pauta.setId(10L);
    }

    @Test
    void deveAbrirSessaoComDuracaoPadraoQuandoNaoInformada() {
        when(pautaService.buscarEntidadePorId(10L)).thenReturn(pauta);
        when(sessaoVotacaoRepository.findByPautaId(10L)).thenReturn(Optional.empty());
        when(sessaoVotacaoRepository.save(any(SessaoVotacao.class))).thenAnswer(chamada -> {
            SessaoVotacao sessao = chamada.getArgument(0);
            sessao.setId(1L);
            return sessao;
        });

        SessaoVotacaoResponseDTO resposta = sessaoVotacaoService.abrir(10L, null);

        assertThat(resposta.getPautaId()).isEqualTo(10L);
        assertThat(resposta.getEncerramentoEm()).isAfter(resposta.getAberturaEm());
    }

    @Test
    void deveAbrirSessaoComDuracaoInformadaPeloUsuario() {
        when(pautaService.buscarEntidadePorId(10L)).thenReturn(pauta);
        when(sessaoVotacaoRepository.findByPautaId(10L)).thenReturn(Optional.empty());
        when(sessaoVotacaoRepository.save(any(SessaoVotacao.class))).thenAnswer(chamada -> chamada.getArgument(0));

        SessaoVotacaoResponseDTO resposta = sessaoVotacaoService.abrir(10L, new AbrirSessaoRequestDTO(5));

        long minutosDeDiferenca = java.time.Duration.between(resposta.getAberturaEm(), resposta.getEncerramentoEm()).toMinutes();
        assertThat(minutosDeDiferenca).isEqualTo(5);
    }

    @Test
    void naoDeveAbrirSessaoSeJaExisteUmaParaAPauta() {
        SessaoVotacao sessaoExistente = new SessaoVotacao(pauta, LocalDateTime.now(), LocalDateTime.now().plusMinutes(1));
        when(pautaService.buscarEntidadePorId(10L)).thenReturn(pauta);
        when(sessaoVotacaoRepository.findByPautaId(10L)).thenReturn(Optional.of(sessaoExistente));

        assertThatThrownBy(() -> sessaoVotacaoService.abrir(10L, null))
                .isInstanceOf(OperacaoInvalidaException.class);
    }
}

