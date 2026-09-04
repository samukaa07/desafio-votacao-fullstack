package com.dbserver.votacao.controller;

import com.dbserver.votacao.client.ValidadorCpfClient;
import com.dbserver.votacao.enums.StatusVotante;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Teste de integracao que simula o fluxo completo: cria pauta, abre sessao,
 * registra voto e confere o resultado. Sobe o contexto Spring inteiro com banco H2 em memoria.
 */
@SpringBootTest
@AutoConfigureMockMvc
class FluxoDeVotacaoIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ValidadorCpfClient validadorCpfClient;

    @BeforeEach
    void garantirQueOAssociadoConsegueVotar() {
        when(validadorCpfClient.consultarSituacaoDoAssociado(org.mockito.ArgumentMatchers.anyString()))
                .thenReturn(StatusVotante.ABLE_TO_VOTE);
    }

    @Test
    void deveExecutarOFluxoCompletoDeCadastroAberturaVotoEResultado() throws Exception {
        String corpoPauta = objectMapper.writeValueAsString(Map.of(
                "titulo", "Aprovacao do balanco anual",
                "descricao", "Votacao sobre as contas do exercicio"
        ));

        String resposta = mockMvc.perform(post("/api/v1/pautas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(corpoPauta))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.titulo").value("Aprovacao do balanco anual"))
                .andReturn().getResponse().getContentAsString();

        Long pautaId = objectMapper.readTree(resposta).get("id").asLong();

        mockMvc.perform(post("/api/v1/pautas/{id}/sessao", pautaId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.pautaId").value(pautaId));

        String corpoVoto = objectMapper.writeValueAsString(Map.of(
                "cpfAssociado", "11122233344",
                "opcao", "SIM"
        ));

        mockMvc.perform(post("/api/v1/pautas/{id}/votos", pautaId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(corpoVoto))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/api/v1/pautas/{id}/resultado", pautaId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalVotosSim").value(1))
                .andExpect(jsonPath("$.totalVotosNao").value(0))
                .andExpect(jsonPath("$.vencedor").value("SIM"));
    }

    @Test
    void naoDevePermitirVotoDuplicadoDoMesmoAssociado() throws Exception {
        String corpoPauta = objectMapper.writeValueAsString(Map.of("titulo", "Pauta com voto duplicado", "descricao", "desc"));
        String resposta = mockMvc.perform(post("/api/v1/pautas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(corpoPauta))
                .andReturn().getResponse().getContentAsString();
        Long pautaId = objectMapper.readTree(resposta).get("id").asLong();

        mockMvc.perform(post("/api/v1/pautas/{id}/sessao", pautaId)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"));

        String corpoVoto = objectMapper.writeValueAsString(Map.of("cpfAssociado", "99988877766", "opcao", "NAO"));

        mockMvc.perform(post("/api/v1/pautas/{id}/votos", pautaId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(corpoVoto))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/api/v1/pautas/{id}/votos", pautaId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(corpoVoto))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void deveRetornarNotFoundParaPautaInexistente() throws Exception {
        mockMvc.perform(get("/api/v1/pautas/{id}", 999999))
                .andExpect(status().isNotFound());
    }
}

