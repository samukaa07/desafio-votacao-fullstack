// Aqui centralizo as chamadas para a API do backend, pra nao ficar fetch espalhado pelos componentes
const URL_BASE = process.env.REACT_APP_API_URL || 'http://localhost:8080/api/v1';

async function tratarResposta(resposta) {
  if (resposta.status === 204) {
    return null;
  }

  const dados = await resposta.json().catch(() => null);

  if (!resposta.ok) {
    const mensagem = dados && dados.mensagem ? dados.mensagem : `Erro ${resposta.status}`;
    throw new Error(mensagem);
  }

  return dados;
}

export async function listarPautas() {
  const resposta = await fetch(`${URL_BASE}/pautas`);
  return tratarResposta(resposta);
}

export async function buscarPauta(id) {
  const resposta = await fetch(`${URL_BASE}/pautas/${id}`);
  return tratarResposta(resposta);
}

export async function cadastrarPauta({ titulo, descricao }) {
  const resposta = await fetch(`${URL_BASE}/pautas`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ titulo, descricao })
  });
  return tratarResposta(resposta);
}

export async function abrirSessao(pautaId, duracaoEmMinutos) {
  const resposta = await fetch(`${URL_BASE}/pautas/${pautaId}/sessao`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(duracaoEmMinutos ? { duracaoEmMinutos } : {})
  });
  return tratarResposta(resposta);
}

export async function buscarSessao(pautaId) {
  const resposta = await fetch(`${URL_BASE}/pautas/${pautaId}/sessao`);
  return tratarResposta(resposta);
}

export async function votar(pautaId, cpfAssociado, opcao) {
  const resposta = await fetch(`${URL_BASE}/pautas/${pautaId}/votos`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ cpfAssociado, opcao })
  });
  return tratarResposta(resposta);
}

export async function buscarResultado(pautaId) {
  const resposta = await fetch(`${URL_BASE}/pautas/${pautaId}/resultado`);
  return tratarResposta(resposta);
}

