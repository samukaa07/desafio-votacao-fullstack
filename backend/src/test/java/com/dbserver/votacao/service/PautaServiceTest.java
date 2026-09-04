package com.dbserver.votacao.service;

import com.dbserver.votacao.dto.PautaRequestDTO;
import com.dbserver.votacao.dto.PautaResponseDTO;
import com.dbserver.votacao.exception.RecursoNaoEncontradoException;
import com.dbserver.votacao.model.Pauta;
import com.dbserver.votacao.repository.PautaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PautaServiceTest {

    @Mock
    private PautaRepository pautaRepository;

    @InjectMocks
    private PautaService pautaService;

    private Pauta pauta;

    @BeforeEach
    void configurarCenario() {
        pauta = new Pauta("Aumento da taxa de administracao", "Discutir reajuste anual");
        pauta.setId(1L);
    }

    @Test
    void deveCadastrarUmaPautaComSucesso() {
        when(pautaRepository.save(any(Pauta.class))).thenReturn(pauta);

        PautaResponseDTO resposta = pautaService.cadastrar(new PautaRequestDTO("Aumento da taxa de administracao", "Discutir reajuste anual"));

        assertThat(resposta.getId()).isEqualTo(1L);
        assertThat(resposta.getTitulo()).isEqualTo("Aumento da taxa de administracao");
        verify(pautaRepository).save(any(Pauta.class));
    }

    @Test
    void deveListarTodasAsPautasCadastradas() {
        when(pautaRepository.findAll()).thenReturn(List.of(pauta));

        List<PautaResponseDTO> pautas = pautaService.listarTodas();

        assertThat(pautas).hasSize(1);
        assertThat(pautas.get(0).getTitulo()).isEqualTo(pauta.getTitulo());
    }

    @Test
    void deveRetornarUmaPautaQuandoIdExiste() {
        when(pautaRepository.findById(1L)).thenReturn(Optional.of(pauta));

        PautaResponseDTO resposta = pautaService.buscarPorId(1L);

        assertThat(resposta.getId()).isEqualTo(1L);
    }

    @Test
    void deveLancarExcecaoQuandoPautaNaoExiste() {
        when(pautaRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> pautaService.buscarPorId(99L))
                .isInstanceOf(RecursoNaoEncontradoException.class)
                .hasMessageContaining("99");
    }
}

