import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { cadastrarPauta } from '../servicos/api';

export default function NovaPauta() {
  const navegar = useNavigate();
  const [titulo, setTitulo] = useState('');
  const [descricao, setDescricao] = useState('');
  const [enviando, setEnviando] = useState(false);
  const [erro, setErro] = useState('');

  async function aoSubmeter(evento) {
    evento.preventDefault();
    setErro('');

    if (!titulo.trim()) {
      setErro('Preencha o título da pauta');
      return;
    }

    try {
      setEnviando(true);
      const pautaCriada = await cadastrarPauta({ titulo, descricao });
      navegar(`/pautas/${pautaCriada.id}`);
    } catch (e) {
      setErro(e.message);
    } finally {
      setEnviando(false);
    }
  }

  return (
    <section>
      <h2>Cadastrar nova pauta</h2>

      <form onSubmit={aoSubmeter} className="formulario">
        <label>
          Título
          <input
            type="text"
            value={titulo}
            onChange={(e) => setTitulo(e.target.value)}
            maxLength={150}
            placeholder="Ex: Mudança no valores financeiros"
          />
        </label>

        <label>
          Descrição
          <textarea
            value={descricao}
            onChange={(e) => setDescricao(e.target.value)}
            maxLength={1000}
            rows={4}
            placeholder="Detalhes relacionado a o que sera votado"
          />
        </label>

        {erro && <p className="mensagem-erro">{erro}</p>}

        <button type="submit" disabled={enviando}>
          {enviando ? 'Salvando...' : 'Cadastrar pauta'}
        </button>
      </form>
    </section>
  );
}

