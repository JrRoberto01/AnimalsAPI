# Plano de Desenvolvimento — API de Adoção de Animais

Este documento divide o desenvolvimento entre três pessoas, com uma camada sob responsabilidade de cada integrante.

## Objetivo

Desenvolver uma API REST em Java com Spring Boot para cadastrar, listar, consultar, atualizar e excluir animais disponíveis para adoção.

## Estrutura esperada

```text
com.example.animalapi
├── domain
│   ├── model
│   │   ├── Animal.java
│   │   └── AnimalId.java
│   ├── exception
│   │   └── AnimalNotFoundException.java
│   └── repository
│       └── AnimalRepository.java
├── application
│   ├── usecase
│   │   ├── CreateAnimalUseCase.java
│   │   ├── GetAnimalByIdUseCase.java
│   │   ├── ListAnimalsUseCase.java
│   │   ├── UpdateAnimalUseCase.java
│   │   └── DeleteAnimalUseCase.java
│   ├── input
│   │   ├── CreateAnimalInput.java
│   │   └── UpdateAnimalInput.java
│   └── output
│       └── AnimalOutput.java
├── infrastructure
│   ├── http
│   │   ├── controller/AnimalController.java
│   │   ├── request/CreateAnimalRequest.java
│   │   ├── request/UpdateAnimalRequest.java
│   │   └── response/AnimalResponse.java
│   ├── persistence
│   │   ├── entity/AnimalJpaEntity.java
│   │   ├── repository/SpringDataAnimalRepository.java
│   │   ├── adapter/AnimalRepositoryAdapter.java
│   │   └── mapper/AnimalPersistenceMapper.java
│   ├── exception/GlobalExceptionHandler.java
│   └── config/BeanConfiguration.java
└── AnimalApiApplication.java
```

## Contrato compartilhado

- Pacote-base: `com.example.animalapi`.
- `AnimalId` encapsula `java.util.UUID`.
- Atributos mínimos de `Animal`:
  - `id`: identificador único.
  - `name`: nome do animal.
  - `species`: espécie, como cachorro ou gato.
  - `breed`: raça.
  - `age`: idade em anos.
  - `availableForAdoption`: disponibilidade para adoção.

## Ordem de dependência e prioridade

| Ordem | Prioridade | Responsável | Entrega | Dependência |
|---:|---|---|---|---|
| 1 | `P0 — Bloqueadora` | Pessoa 1 | Domain | Nenhuma |
| 2 | `P0 — Alta` | Pessoa 2 | Application | Contratos da Domain |
| 3 | `P1 — Alta` | Pessoa 3 | Infrastructure | Domain e Application |
| 4 | `P0 — Liberação` | Equipe | Integração e testes | Três camadas concluídas |

```text
Domain ──> Application ──> Infrastructure ──> Validação integrada
```

As implementações podem ser preparadas em paralelo, mas a integração deve respeitar a sequência acima. Alterações em contratos compartilhados devem ser comunicadas às pessoas responsáveis pelas camadas dependentes.

---

## Task 1 — Camada Domain

**Prioridade:** `P0 — Bloqueadora`  
**Responsável:** Pessoa 1  
**Dependências:** Nenhuma  
**Bloqueia:** Application e Infrastructure

### Objetivo

Implementar o núcleo da API de adoção sem dependências de Spring, JPA, banco de dados ou HTTP.

### Arquivos

```text
domain/
├── model/
│   ├── Animal.java
│   └── AnimalId.java
├── exception/
│   └── AnimalNotFoundException.java
└── repository/
    └── AnimalRepository.java
```

### Atividades

- Criar `AnimalId` encapsulando um `UUID`.
- Criar `Animal` com `id`, `name`, `species`, `breed`, `age` e `availableForAdoption`.
- Validar nome e espécie obrigatórios e idade igual ou maior que zero.
- Permitir atualização controlada dos dados do animal.
- Criar `AnimalNotFoundException`, contendo o ID não encontrado.
- Criar a porta `AnimalRepository` com operações equivalentes a:

```java
Animal save(Animal animal);
Optional<Animal> findById(AnimalId id);
List<Animal> findAll();
void deleteById(AnimalId id);
```

### Restrições

- Utilizar Java puro.
- Não importar Spring, JPA, Application ou Infrastructure.
- Não implementar banco de dados ou endpoints.

### Critérios de aceite

- [ ] Domain compila sem frameworks.
- [ ] As regras do modelo estão protegidas.
- [ ] A interface de repositório não contém implementação técnica.
- [ ] As regras relevantes possuem testes unitários.
- [ ] As assinaturas públicas foram comunicadas à Pessoa 2.

---

## Task 2 — Camada Application

**Prioridade:** `P0 — Alta`  
**Responsável:** Pessoa 2  
**Dependências:** Task 1 — Domain  
**Bloqueia:** Integração completa da Infrastructure

### Objetivo

Implementar os casos de uso que coordenam o CRUD de animais por meio de `AnimalRepository`.

### Arquivos

```text
application/
├── usecase/
│   ├── CreateAnimalUseCase.java
│   ├── GetAnimalByIdUseCase.java
│   ├── ListAnimalsUseCase.java
│   ├── UpdateAnimalUseCase.java
│   └── DeleteAnimalUseCase.java
├── input/
│   ├── CreateAnimalInput.java
│   └── UpdateAnimalInput.java
└── output/
    └── AnimalOutput.java
```

### Atividades

- Criar inputs com `name`, `species`, `breed`, `age` e `availableForAdoption`.
- Criar `AnimalOutput` com os mesmos dados e o `UUID id`.
- Implementar os cinco casos de uso.
- Converter o modelo de domínio em `AnimalOutput`.
- Lançar `AnimalNotFoundException` ao consultar, atualizar ou excluir um ID inexistente.
- Criar testes com mock ou repositório fake em `src/test`.

### Restrições

- Depender apenas da porta `AnimalRepository`.
- Não utilizar Spring Data, JPA, controllers ou DTOs HTTP.
- Não duplicar classes da Domain ou Infrastructure.

### Critérios de aceite

- [ ] Os cinco casos de uso estão implementados.
- [ ] Os casos de uso não conhecem HTTP ou banco de dados.
- [ ] IDs inexistentes geram `AnimalNotFoundException`.
- [ ] Os testes unitários cobrem os fluxos principais.
- [ ] As assinaturas de inputs, output e casos de uso foram comunicadas à Pessoa 3.

---

## Task 3 — Camada Infrastructure

**Prioridade:** `P1 — Alta`  
**Responsável:** Pessoa 3  
**Dependências:** Tasks 1 e 2  
**Bloqueia:** Validação e apresentação final

### Objetivo

Implementar a API REST, persistência com Spring Data JPA, banco H2, tratamento de erros e configuração da aplicação.

### Atividades

- Criar `AnimalJpaEntity` e `SpringDataAnimalRepository`.
- Implementar `AnimalRepositoryAdapter`.
- Mapear `Animal` para `AnimalJpaEntity` e vice-versa.
- Criar requests e responses separados das entidades.
- Validar requests com Jakarta Validation.
- Criar `AnimalController`.
- Tratar validação, UUID inválido e `AnimalNotFoundException`.
- Criar `BeanConfiguration` para instanciar os casos de uso.
- Configurar H2 e ajustar o `pom.xml` quando necessário.
- Garantir que `AnimalApiApplication` esteja no pacote-base correto.

### Endpoints obrigatórios

| Método | Endpoint | Resultado |
|---|---|---|
| `POST` | `/animals` | Cadastrar — `201 Created` |
| `GET` | `/animals` | Listar — `200 OK` |
| `GET` | `/animals/{id}` | Consultar — `200 OK` ou `404 Not Found` |
| `PUT` | `/animals/{id}` | Atualizar — `200 OK` ou `404 Not Found` |
| `DELETE` | `/animals/{id}` | Excluir — `204 No Content` ou `404 Not Found` |

### Tratamento de erros

- Dados inválidos: `400 Bad Request`.
- UUID inválido: `400 Bad Request`.
- Animal não encontrado: `404 Not Found`.
- Erros devem ser devolvidos em formato JSON consistente.

### Restrições

- Não retornar entidades JPA pelo controller.
- Não enviar DTOs HTTP para Domain ou Application.
- Não duplicar classes pertencentes às outras camadas.

### Critérios de aceite

- [ ] Os cinco endpoints estão conectados aos casos de uso.
- [ ] A persistência funciona com H2.
- [ ] Requests inválidos retornam `400`.
- [ ] Animais inexistentes retornam `404`.
- [ ] A aplicação inicia sem erros.

---

## Task 4 — Integração e validação final

**Prioridade:** `P0 — Liberação`  
**Responsável:** Equipe  
**Dependências:** Tasks 1, 2 e 3 concluídas

### Ordem de integração

1. Integrar Domain.
2. Integrar Application.
3. Integrar Infrastructure.
4. Corrigir incompatibilidades entre contratos.
5. Executar todos os testes.
6. Validar manualmente os endpoints.

### Checklist de liberação

- [ ] O projeto compila com `./mvnw clean test`.
- [ ] A aplicação inicia com `./mvnw spring-boot:run`.
- [ ] É possível cadastrar um animal.
- [ ] É possível listar os animais.
- [ ] É possível consultar um animal pelo ID.
- [ ] É possível atualizar um animal.
- [ ] É possível excluir um animal.
- [ ] A disponibilidade para adoção é armazenada e retornada corretamente.
- [ ] A estrutura final utiliza `com.example.animalapi`.

