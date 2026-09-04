import React, { useCallback, useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import {
  buscarPauta,
  buscarSessao,
  abrirSessao,
  votar,
  buscarResultado
} from '../servicos/api';

export default function DetalhesDaPauta() {
  const { id } = useParams();

  const [pauta, setPauta] = useState(null);
  const [sessao, setSessao] = useState(null);
  const [resultado, setResultado] = useState(null);
  const [erro, setErro] = useState('');
  const [aviso, setAviso] = useState('');

  const [duracaoMinutos, setDuracaoMinutos] = useState('');
  const [cpf, setCpf] = useState('');
  const [opcaoEscolhida, setOpcaoEscolhida] = useState('SIM');
  const [enviandoVoto, setEnviandoVoto] = useState(false);

  const carregarDados = useCallback(() => {
    buscarPauta(id).then(setPauta).catch((e) => setErro(e.message));

    buscarSessao(id)
      .then((s) => {
        setSessao(s);
        // se a sessao ja encerrou, aproveita e busca o resultado direto
        if (s && s.status === 'ENCERRADA') {
          buscarResultado(id).then(setResultado).catch(() => {});
        }
      })
      .catch(() => setSessao(null));
  }, [id]);

  useEffect(() => {
    carregarDados();
  }, [carregarDados]);

  // fica de olho se a sessao encerrou enquanto a tela ta aberta
  useEffect(() => {
    if (!sessao || sessao.status === 'ENCERRADA') {
      return;
    }

    const intervalo = setInterval(() => {
      const encerraEm = new Date(sessao.encerramentoEm).getTime();
      if (Date.now() >= encerraEm) {
        carregarDados();
      }
    }, 2000);

    return () => clearInterval(intervalo);
  }, [sessao, carregarDados]);

  async function aoAbrirSessao() {
    setErro('');
    try {
      const minutos = duracaoMinutos ? Number(duracaoMinutos) : undefined;
      const novaSessao = await abrirSessao(id, minutos);
      setSessao(novaSessao);
    } catch (e) {
      setErro(e.message);
    }
  }

  async function aoVotar(evento) {
    evento.preventDefault();
    setErro('');
    setAviso('');

    if (!/^\d{11}$/.test(cpf)) {
      setErro('Informe um CPF valido, apenas números (11 dígitos)');
      return;
    }

    try {
      setEnviandoVoto(true);
      await votar(id, cpf, opcaoEscolhida);
      setAviso('Voto registrado com sucesso!');
      setCpf('');
    } catch (e) {
      setErro(e.message);
    } finally {
      setEnviandoVoto(false);
    }
  }

  async function aoVerResultado() {
    setErro('');
    try {
      const dados = await buscarResultado(id);
      setResultado(dados);
    } catch (e) {
      setErro(e.message);
    }
  }

  if (!pauta) {
    return <p>Carregando pauta...</p>;
  }

  const sessaoEstaAberta = sessao && sessao.status === 'ABERTA';

  return (
    <section>
      <h2>{pauta.titulo}</h2>
      <p>{pauta.descricao}</p>

      {erro && <p className="mensagem-erro">{erro}</p>}
      {aviso && <p className="mensagem-sucesso">{aviso}</p>}

      {!sessao && (
        <div className="bloco">
          <h3>Sessão de votação ainda não foi aberta</h3>
          <label>
            Duração (minutos, opcional - padrão é 1 minuto)
            <input
              type="number"
              min="1"
              value={duracaoMinutos}
              onChange={(e) => setDuracaoMinutos(e.target.value)}
            />
          </label>
          <button onClick={aoAbrirSessao}>Abrir sessão</button>
        </div>
      )}

      {sessao && (
        <div className="bloco">
          <h3>Sessão de votação</h3>
          <p>Status: <strong>{sessao.status}</strong></p>
          <p>Encerra em: {new Date(sessao.encerramentoEm).toLocaleString()}</p>
        </div>
      )}

      {sessaoEstaAberta && (
        <form onSubmit={aoVotar} className="formulario">
          <h3>Registrar voto</h3>
          <p className="dica">
            A validação do CPF simula um serviço externo instável: às vezes ele recusa o voto
            aleatoriamente (CPF "não encontrado" ou associado "não apto"). Se isso acontecer,
            é só tentar votar de novo com o mesmo CPF.
          </p>
          <label>
            CPF do associado
            <input
              type="text"
              value={cpf}
              onChange={(e) => setCpf(e.target.value.replace(/\D/g, ''))}
              maxLength={11}
              placeholder="Somente números"
            />
          </label>

          <label>
            Voto
            <select value={opcaoEscolhida} onChange={(e) => setOpcaoEscolhida(e.target.value)}>
              <option value="SIM">Sim</option>
              <option value="NAO">Não</option>
            </select>
          </label>

          <button type="submit" disabled={enviandoVoto}>
            {enviandoVoto ? 'Enviando...' : 'Votar'}
          </button>
        </form>
      )}

      {sessao && sessao.status === 'ENCERRADA' && (
        <div className="bloco">
          <button onClick={aoVerResultado}>Ver resultado</button>

          {resultado && (
            <div className="resultado">
              <p>Votos Sim: {resultado.totalVotosSim}</p>
              <p>Votos Não: {resultado.totalVotosNao}</p>
              <p>Resultado final: <strong>{resultado.vencedor}</strong></p>
            </div>
          )}
        </div>
      )}
    </section>
  );
}

