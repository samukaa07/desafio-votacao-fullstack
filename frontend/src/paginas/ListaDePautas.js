import React, { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import { listarPautas } from '../servicos/api';

export default function ListaDePautas() {
  const [pautas, setPautas] = useState([]);
  const [carregando, setCarregando] = useState(true);
  const [erro, setErro] = useState('');

  useEffect(() => {
    carregarPautas();
  }, []);

  function carregarPautas() {
    setCarregando(true);
    listarPautas()
      .then((dados) => setPautas(dados || []))
      .catch((e) => setErro(e.message))
      .finally(() => setCarregando(false));
  }

  if (carregando) {
    return <p>Carregando pautas...</p>;
  }

  return (
    <section>
      <h2>Pautas cadastradas</h2>

      {erro && <p className="mensagem-erro">{erro}</p>}

      {pautas.length === 0 && !erro && (
        <p>Nenhuma pauta cadastrada ainda. Que tal criar a primeira?</p>
      )}

      <ul className="lista-pautas">
        {pautas.map((pauta) => (
          <li key={pauta.id} className="card-pauta">
            <Link to={`/pautas/${pauta.id}`}>
              <strong>{pauta.titulo}</strong>
              <p>{pauta.descricao}</p>
            </Link>
          </li>
        ))}
      </ul>
    </section>
  );
}

