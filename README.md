# Votação

## Objetivo

No cooperativismo, cada associado possui um voto e as decisões são tomadas em assembleias, por votação. Imagine que você deve criar uma solução we para gerenciar e participar dessas sessões de votação.
Essa solução deve ser executada na nuvem e promover as seguintes funcionalidades através de uma API REST / Front:

- Cadastrar uma nova pauta
- Abrir uma sessão de votação em uma pauta (a sessão de votação deve ficar aberta por
  um tempo determinado na chamada de abertura ou 1 minuto por default)
- Receber votos dos associados em pautas (os votos são apenas 'Sim'/'Não'. Cada associado
  é identificado por um id único e pode votar apenas uma vez por pauta)
- Contabilizar os votos e dar o resultado da votação na pauta

Para fins de exercício, a segurança das interfaces pode ser abstraída e qualquer chamada para as interfaces pode ser considerada como autorizada. A solução deve ser construída em java com Spring-boot e Angular/React conforme orientação, mas os frameworks e bibliotecas são de livre escolha (desde que não infrinja direitos de uso).

É importante que as pautas e os votos sejam persistidos e que não sejam perdidos com o restart da aplicação.

## Como proceder

Por favor, realize o FORK desse repositório e implemente sua solução no FORK em seu repositório GItHub, ao final, notifique da conclusão para que possamos analisar o código implementado.

Lembre de deixar todas as orientações necessárias para executar o seu código.

### Tarefas bônus

- Tarefa Bônus 1 - Integração com sistemas externos
  - Criar uma Facade/Client Fake que retorna aleátoriamente se um CPF recebido é válido ou não.
  - Caso o CPF seja inválido, a API retornará o HTTP Status 404 (Not found). Você pode usar geradores de CPF para gerar CPFs válidos
  - Caso o CPF seja válido, a API retornará se o usuário pode (ABLE_TO_VOTE) ou não pode (UNABLE_TO_VOTE) executar a operação. Essa operação retorna resultados aleatórios, portanto um mesmo CPF pode funcionar em um teste e não funcionar no outro.

```
// CPF Ok para votar
{
    "status": "ABLE_TO_VOTE
}
// CPF Nao Ok para votar - retornar 404 no client tb
{
    "status": "UNABLE_TO_VOTE
}
```

Exemplos de retorno do serviço

### Tarefa Bônus 2 - Performance

- Imagine que sua aplicação possa ser usada em cenários que existam centenas de
  milhares de votos. Ela deve se comportar de maneira performática nesses
  cenários
- Testes de performance são uma boa maneira de garantir e observar como sua
  aplicação se comporta

### Tarefa Bônus 3 - Versionamento da API

○ Como você versionaria a API da sua aplicação? Que estratégia usar?

## O que será analisado

- Simplicidade no design da solução (evitar over engineering)
- Organização do código
- Arquitetura do projeto
- Boas práticas de programação (manutenibilidade, legibilidade etc)
- Possíveis bugs
- Tratamento de erros e exceções
- Explicação breve do porquê das escolhas tomadas durante o desenvolvimento da solução
- Uso de testes automatizados e ferramentas de qualidade
- Limpeza do código
- Documentação do código e da API
- Logs da aplicação
- Mensagens e organização dos commits
- Testes
- Layout responsivo

## Dicas

- Teste bem sua solução, evite bugs

  Observações importantes
- Não inicie o teste sem sanar todas as dúvidas
- Iremos executar a aplicação para testá-la, cuide com qualquer dependência externa e
  deixe claro caso haja instruções especiais para execução do mesmo
  Classificação da informação: Uso Interno





---

## Solução implementada

Este fork contém a implementação do desafio, dividida em duas pastas:

- `backend/` - API REST em Java 17 + Spring Boot 3
- `frontend/` - interface em React que consome a API

### Backend

**Stack:** Spring Boot 3, Spring Data JPA, Bean Validation, H2 (arquivo, para persistir entre restarts), springdoc-openapi (Swagger).

Estrutura de pacotes (`com.dbserver.votacao`):

- `model` - entidades JPA (`Pauta`, `SessaoVotacao`, `Voto`)
- `repository` - repositórios Spring Data
- `service` - regras de negócio (`PautaService`, `SessaoVotacaoService`, `VotoService`, `ResultadoVotacaoService`)
- `controller` - endpoints REST
- `dto` - objetos de entrada/saída da API
- `client` - `ValidadorCpfClient`, client fake que simula a integração externa de validação de CPF (tarefa bônus 1)
- `exception` - exceções de negócio e o `@RestControllerAdvice` que converte tudo em respostas HTTP coerentes

Endpoints principais (prefixo `/api/v1`):

| Método | Rota | Descrição |
|---|---|---|
| POST | `/pautas` | Cadastra uma pauta |
| GET | `/pautas` | Lista todas as pautas |
| GET | `/pautas/{id}` | Busca uma pauta |
| POST | `/pautas/{id}/sessao` | Abre a sessão de votação (corpo opcional `{ "duracaoEmMinutos": 5 }`, default 1 minuto) |
| GET | `/pautas/{id}/sessao` | Consulta a sessão da pauta |
| POST | `/pautas/{id}/votos` | Registra o voto de um associado (`{ "cpfAssociado": "12345678900", "opcao": "SIM" }`) |
| GET | `/pautas/{id}/resultado` | Apura o resultado (votos Sim/Não e vencedor) |

Documentação interativa (Swagger UI) disponível em `/swagger-ui.html` com a aplicação rodando.

**Persistência:** o H2 roda em modo arquivo (`./backend/data/votacao.mv.db`), então os dados sobrevivem a um restart da aplicação, como pede o desafio.

**Validação de CPF (bônus 1):** `ValidadorCpfClient` sorteia aleatoriamente se o CPF é válido (senão lança exceção que vira `404`) e, sendo válido, sorteia se o associado está `ABLE_TO_VOTE` ou `UNABLE_TO_VOTE`.

**Versionamento da API (bônus 3):** optei por versionamento via URI (`/api/v1/...`). É a abordagem mais simples de explicar, testar e documentar, além de deixar bem visível pro consumidor da API qual contrato ele está usando. Uma v2 poderia conviver junto sem quebrar quem já integrou com a v1.

**Sobre performance (bônus 2):** o resultado é apurado com `COUNT` no banco (e não trazendo todos os votos pra memória), e o índice único em `(sessao_id, cpf_associado)` evita voto duplicado e acelera a checagem. Para o cenário de centenas de milhares de votos, os pontos de atenção seriam: paginação na listagem de pautas, índice composto (já criado via `@UniqueConstraint`) e, se necessário, mover a contagem para um contador incremental atualizado a cada voto.

#### Como rodar o backend

```
cd backend
mvn spring-boot:run
```

A API sobe em `http://localhost:8080`.

#### Como rodar os testes do backend

```
cd backend
mvn test
```

Foram feitos testes unitários (Mockito, para as regras de negócio dos services) e testes de integração (`@SpringBootTest` + `MockMvc`, cobrindo o fluxo completo: cadastrar pauta → abrir sessão → votar → apurar resultado, além de cenários de erro como voto duplicado e pauta inexistente).

### Frontend

**Stack:** React 18 (Create React App) + React Router. Interface simples e responsiva com três telas: lista de pautas, cadastro de pauta e detalhes da pauta (onde é possível abrir a sessão, votar e ver o resultado).

#### Como rodar o frontend

```
cd frontend
npm install
npm start
```

A aplicação sobe em `http://localhost:3000` e já aponta para `http://localhost:8080/api/v1` (configurável em `frontend/.env`).

---

# desafio-votacao
