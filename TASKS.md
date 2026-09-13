# Plano de Desenvolvimento por Dependência e Prioridade — Movie API

Este documento divide o desenvolvimento entre três pessoas, com uma camada sob responsabilidade de cada integrante.

> A estrutura explícita `com.example.movieapi` prevalece. A imagem anexada foi considerada como referência para os requisitos gerais: Java, Spring Boot e API REST com operações CRUD.

## Ordem obrigatória de execução

| Ordem | Prioridade | Responsável | Entrega | Depende de |
|---:|---|---|---|---|
| 1 | `P0 — Bloqueadora` | Pessoa 1 | Camada Domain e contratos centrais | Nenhuma |
| 2 | `P0 — Alta` | Pessoa 2 | Camada Application e casos de uso | Contratos da Domain estabilizados |
| 3 | `P1 — Alta` | Pessoa 3 | Camada Infrastructure, API REST e persistência | Domain e Application integradas |
| 4 | `P0 — Liberação` | Equipe | Integração, testes e validação do CRUD | Entregas das três pessoas |

```text
Domain ──> Application ──> Infrastructure ──> Validação integrada
   │             │                  │
   └─ modelos    └─ casos de uso    └─ REST, JPA, H2 e configuração
      e porta       e DTOs internos
```

### Regra para trabalho em paralelo

As pessoas podem preparar suas implementações simultaneamente, mas devem respeitar os contratos deste documento. A ordem de integração deve ser Domain, Application e Infrastructure. Mudanças em contratos compartilhados precisam ser comunicadas antes de serem utilizadas pelas camadas dependentes.

## Contrato compartilhado

- Pacote-base: `com.example.movieapi`
- Identificador: `MovieId`, encapsulando `java.util.UUID`
- Atributos do filme:
  - `id`
  - `title`
  - `director`
  - `releaseYear`
- Operações obrigatórias:
  - Cadastrar um filme
  - Listar todos os filmes
  - Consultar um filme pelo ID
  - Atualizar um filme
  - Excluir um filme

---

## Task 1 — Desenvolver a camada Domain (`P0 — Bloqueadora`)

**Responsável:** Pessoa 1  
**Objetivo:** Implementar o núcleo de domínio da API de filmes, sem dependências de Spring, JPA ou HTTP.

**Dependências:** Nenhuma. Esta é a primeira entrega.  
**Bloqueia:** Tasks 2 e 3.  
**Condição para iniciar:** Projeto Maven disponível.  
**Condição para liberar a próxima Task:** contratos públicos compilando, testados e comunicados à equipe.

### Arquivos

```text
domain/
├── model/
│   ├── Movie.java
│   └── MovieId.java
├── exception/
│   └── MovieNotFoundException.java
└── repository/
    └── MovieRepository.java
```

### Atividades

- Criar `MovieId` encapsulando um `UUID`.
- Criar `Movie` com os atributos:
  - `MovieId id`
  - `String title`
  - `String director`
  - `Integer releaseYear`
- Implementar validações e invariantes básicas do domínio.
- Permitir a criação e atualização de um filme sem utilizar setters indiscriminadamente.
- Criar `MovieNotFoundException`, informando o ID que não foi encontrado.
- Criar a interface `MovieRepository` com operações equivalentes a:

```java
Movie save(Movie movie);
Optional<Movie> findById(MovieId id);
List<Movie> findAll();
void deleteById(MovieId id);
```

### Restrições

- Utilizar Java puro.
- Não importar classes do Spring, JPA, Application ou Infrastructure.
- Não implementar persistência ou endpoints.
- Não alterar arquivos pertencentes às outras camadas.

### Critérios de aceite

- O domínio compila sem depender de frameworks.
- `MovieId` aceita e converte valores UUID.
- Não é possível criar ou atualizar um filme com dados inválidos.
- O repositório representa apenas um contrato, sem implementação técnica.
- Existem testes unitários para as regras relevantes do domínio.

### Handoff para a Pessoa 2

Ao concluir, informar as assinaturas finais de:

- Construtores ou fábricas de `Movie` e `MovieId`.
- Método utilizado para atualizar um filme.
- Métodos de `MovieRepository`.
- Construtor e comportamento de `MovieNotFoundException`.

---

## Task 2 — Desenvolver a camada Application (`P0 — Alta`)

**Responsável:** Pessoa 2  
**Objetivo:** Implementar os casos de uso do CRUD de filmes, coordenando o domínio por meio da interface `MovieRepository`.

**Dependências:** Task 1 — modelos, exceção e porta de repositório do Domain.  
**Bloqueia:** Integração completa da Task 3.  
**Condição para iniciar:** assinaturas públicas da Domain definidas.  
**Condição para liberar a próxima Task:** cinco casos de uso compilando e seus contratos de entrada/saída estabilizados.

### Arquivos

```text
application/
├── usecase/
│   ├── CreateMovieUseCase.java
│   ├── GetMovieByIdUseCase.java
│   ├── ListMoviesUseCase.java
│   ├── UpdateMovieUseCase.java
│   └── DeleteMovieUseCase.java
├── input/
│   ├── CreateMovieInput.java
│   └── UpdateMovieInput.java
└── output/
    └── MovieOutput.java
```

### Atividades

- Criar `CreateMovieInput` com `title`, `director` e `releaseYear`.
- Criar `UpdateMovieInput` com os mesmos campos.
- Criar `MovieOutput` com:
  - `UUID id`
  - `String title`
  - `String director`
  - `Integer releaseYear`
- Implementar `CreateMovieUseCase`.
- Implementar `GetMovieByIdUseCase`.
- Implementar `ListMoviesUseCase`.
- Implementar `UpdateMovieUseCase`.
- Implementar `DeleteMovieUseCase`.
- Converter entidades de domínio em `MovieOutput`.
- Lançar `MovieNotFoundException` nas operações de consulta, atualização ou exclusão quando o ID não existir.

### Dependências esperadas da camada Domain

```text
Movie
MovieId
MovieRepository
MovieNotFoundException
```

### Restrições

- Os casos de uso devem depender somente de `MovieRepository`.
- Não utilizar Spring Data, JPA, controllers ou DTOs HTTP.
- Não implementar ou duplicar classes da camada Domain.
- Não alterar arquivos da camada Infrastructure.

### Critérios de aceite

- Os cinco casos de uso do CRUD estão implementados.
- Nenhum caso de uso conhece detalhes de banco de dados ou HTTP.
- Filmes inexistentes resultam em `MovieNotFoundException`.
- Os casos de uso possuem testes unitários usando mock ou repositório fake em `src/test`.

### Handoff para a Pessoa 3

Ao concluir, informar as assinaturas finais de:

- Construtores dos cinco casos de uso.
- Métodos públicos usados para executar cada caso de uso.
- Campos de `CreateMovieInput`, `UpdateMovieInput` e `MovieOutput`.
- Exceções propagadas para a camada HTTP.

---

## Task 3 — Desenvolver a camada Infrastructure (`P1 — Alta`)

**Responsável:** Pessoa 3  
**Objetivo:** Implementar a API REST, a persistência com Spring Data JPA, o tratamento de erros e a configuração da aplicação.

**Dependências:** Task 1 para os contratos de domínio e Task 2 para os casos de uso.  
**Bloqueia:** Validação integrada e apresentação final.  
**Condição para iniciar:** contratos de Domain e Application estabilizados.  
**Condição para liberar a próxima etapa:** aplicação iniciando, banco H2 funcionando e endpoints conectados aos casos de uso.

### Arquivos

```text
infrastructure/
├── http/
│   ├── controller/
│   │   └── MovieController.java
│   ├── request/
│   │   ├── CreateMovieRequest.java
│   │   └── UpdateMovieRequest.java
│   └── response/
│       └── MovieResponse.java
├── persistence/
│   ├── entity/
│   │   └── MovieJpaEntity.java
│   ├── repository/
│   │   └── SpringDataMovieRepository.java
│   ├── adapter/
│   │   └── MovieRepositoryAdapter.java
│   └── mapper/
│       └── MoviePersistenceMapper.java
├── exception/
│   └── GlobalExceptionHandler.java
└── config/
    └── BeanConfiguration.java

MovieApiApplication.java
```

### Atividades

- Criar `MovieJpaEntity` com mapeamento JPA.
- Criar `SpringDataMovieRepository`.
- Implementar `MovieRepositoryAdapter`, atendendo ao contrato definido no Domain.
- Criar `MoviePersistenceMapper` para converter entre `Movie` e `MovieJpaEntity`.
- Criar os DTOs `CreateMovieRequest`, `UpdateMovieRequest` e `MovieResponse`.
- Adicionar validações com Jakarta Validation.
- Criar `MovieController`.
- Criar `GlobalExceptionHandler`.
- Criar `BeanConfiguration` para instanciar e injetar os casos de uso.
- Criar ou ajustar `MovieApiApplication`.
- Configurar o banco H2 em `application.properties`.
- Ajustar o `pom.xml` somente quando necessário para Spring Web, Validation, JPA e H2.
- Migrar o pacote principal de `com.animal.demo` para `com.example.movieapi`.

### Endpoints obrigatórios

| Método | Endpoint | Resultado esperado |
|---|---|---|
| `POST` | `/movies` | Cadastrar filme — `201 Created` |
| `GET` | `/movies` | Listar filmes — `200 OK` |
| `GET` | `/movies/{id}` | Consultar por ID — `200 OK` ou `404 Not Found` |
| `PUT` | `/movies/{id}` | Atualizar filme — `200 OK` ou `404 Not Found` |
| `DELETE` | `/movies/{id}` | Excluir filme — `204 No Content` ou `404 Not Found` |

### Tratamento de erros

- Dados inválidos: HTTP `400 Bad Request`.
- UUID inválido: HTTP `400 Bad Request`.
- Filme não encontrado: HTTP `404 Not Found`.
- Respostas de erro devem utilizar um formato JSON consistente.

### Restrições

- A entidade JPA não pode ser retornada diretamente pelo controller.
- DTOs HTTP não podem entrar nas camadas Domain ou Application.
- Não duplicar classes pertencentes às outras camadas.
- Os casos de uso devem ser configurados em `BeanConfiguration`.

### Critérios de aceite

- Todos os endpoints funcionam com o banco H2.
- Requests inválidos retornam `400`.
- IDs inexistentes retornam `404`.
- A API executa com `./mvnw spring-boot:run`.
- Os testes Maven passam após a integração das três camadas.

---

## Task 4 — Integração e validação final (`P0 — Liberação`)

**Responsável:** Equipe, após as três entregas individuais.  
**Objetivo:** Integrar as camadas na ordem correta e comprovar todos os requisitos funcionais.

### Dependências

- Task 1 concluída e integrada.
- Task 2 concluída e integrada.
- Task 3 concluída e integrada.

### Ordem de integração

1. Domain
2. Application
3. Infrastructure
4. Ajuste de eventuais incompatibilidades entre contratos
5. Execução dos testes automatizados
6. Validação manual dos cinco endpoints CRUD

### Checklist de liberação

- [ ] O projeto compila com `./mvnw clean test`.
- [ ] A aplicação inicia com `./mvnw spring-boot:run`.
- [ ] `POST /movies` cadastra um filme e retorna `201`.
- [ ] `GET /movies` lista os filmes e retorna `200`.
- [ ] `GET /movies/{id}` consulta um filme e trata `404`.
- [ ] `PUT /movies/{id}` atualiza um filme e trata `404`.
- [ ] `DELETE /movies/{id}` exclui um filme e retorna `204`.
- [ ] Requests inválidos retornam `400` em JSON.
- [ ] As entidades JPA não aparecem nos contratos HTTP.
- [ ] Domain não possui dependências de Spring ou JPA.
- [ ] Não existem classes duplicadas nos pacotes antigos e novos.

### Definição de concluído

O desenvolvimento estará concluído quando os testes estiverem passando, os cinco endpoints puderem ser demonstrados e a separação entre Domain, Application e Infrastructure estiver preservada.
