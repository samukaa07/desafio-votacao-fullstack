import React from 'react';
import { BrowserRouter, Routes, Route, Link } from 'react-router-dom';
import ListaDePautas from './paginas/ListaDePautas';
import DetalhesDaPauta from './paginas/DetalhesDaPauta';
import NovaPauta from './paginas/NovaPauta';

export default function App() {
  return (
    <BrowserRouter>
      <div className="pagina-app">
        <header className="cabecalho">
          <div className="marca">
            <span className="marca-icone">🗳️</span>
            <div>
              <h1>Assembleia</h1>
              <p className="marca-subtitulo">sistema de votação</p>
            </div>
          </div>
          <nav>
            <Link to="/">Pautas</Link>
            <Link to="/nova-pauta">+ Nova pauta</Link>
          </nav>
        </header>

        <main className="conteudo">
          <Routes>
            <Route path="/" element={<ListaDePautas />} />
            <Route path="/nova-pauta" element={<NovaPauta />} />
            <Route path="/pautas/:id" element={<DetalhesDaPauta />} />
          </Routes>
        </main>

        <footer className="rodape">
          <span>feito por Samuel · desafio fullstack dbserver</span>
        </footer>
      </div>
    </BrowserRouter>
  );
}

