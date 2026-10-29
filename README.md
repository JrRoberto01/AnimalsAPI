# Books API — Gerenciamento de Livros

API REST para organizar uma biblioteca pessoal. Utiliza Java 25, Spring Boot 4.1.1, Maven, Spring MVC, Bean Validation, Spring Data JPA e H2.

## Executar


```powershell
.\mvnw.cmd spring-boot:run
```
A API fica disponível em `http://localhost:8080/books`.

## Operações

| Método | Caminho | Funcionalidade | Sucesso |
| --- | --- | --- | --- |
| POST | `/books` | Cadastrar livro | 201 e cabeçalho Location |
| GET | `/books` | Listar todos os livros | 200 e lista JSON |
| GET | `/books/{id}` | Consultar por UUID | 200 |
| PUT | `/books/{id}` | Atualizar todos os campos | 200 |
| DELETE | `/books/{id}` | Excluir livro | 204 sem corpo |

O identificador é um UUID gerado pelo sistema. Consulta, atualização e exclusão de um identificador inexistente retornam 404. Dados inválidos retornam 400. Uma biblioteca vazia retorna `[]`.

Corpo JSON para cadastro e atualização:

```json
{
  "titulo": "Dom Casmurro",
  "autor": "Machado de Assis",
  "genero": "Romance",
  "anoPublicacao": 1899,
  "statusLeitura": "NAO_LIDO"
}
```

Todos os campos são obrigatórios. Título, autor e gênero não podem estar em branco e aceitam até 200, 150 e 80 caracteres, respectivamente. O ano deve estar entre 1 e o ano atual. Os valores de status aceitos são `NAO_LIDO`, `LENDO` e `LIDO` (em maiúsculas). O PUT exige o objeto completo.

## Demonstração no PowerShell

Com a aplicação em execução, abra outro terminal:

```powershell
$base = 'http://localhost:8080/books'
$dados = @{
    titulo = 'Dom Casmurro'
    autor = 'Machado de Assis'
    genero = 'Romance'
    anoPublicacao = 1899
    statusLeitura = 'NAO_LIDO'
}

# Cadastrar e guardar o identificador retornado
$livro = Invoke-RestMethod -Method Post -Uri $base -ContentType 'application/json; charset=utf-8' -Body ($dados | ConvertTo-Json)
$livro

# Listar e consultar
Invoke-RestMethod -Uri $base
Invoke-RestMethod -Uri "$base/$($livro.id)"

# Atualizar o status de leitura
$dados.statusLeitura = 'LIDO'
Invoke-RestMethod -Method Put -Uri "$base/$($livro.id)" -ContentType 'application/json; charset=utf-8' -Body ($dados | ConvertTo-Json)

# Excluir e confirmar que a lista voltou a ficar vazia
Invoke-RestMethod -Method Delete -Uri "$base/$($livro.id)"
Invoke-RestMethod -Uri $base
```

## Testes automatizados

```powershell
.\mvnw.cmd clean verify
```

Os testes cobrem regras do domínio, os cinco casos de uso, inicialização da aplicação e integração dos endpoints com o banco H2: fluxo completo de cadastro a exclusão, persistência dos três status, consultas inexistentes, UUID inválido, campos ausentes, limites de texto, ano inválido, JSON malformado e atualização inválida sem alteração dos dados.

Os relatórios são gerados em `target/surefire-reports`. Os testes usam um banco em memória separado do banco da aplicação.

Para gerar e executar o JAR:

```powershell
.\mvnw.cmd package
java -jar target/books-api-0.0.1-SNAPSHOT.jar
```

## Organização e orientação a objetos

- `domain`: classe `Book` com atributos privados, construtores, métodos de consulta e atualização; `BookId` como objeto de valor; enum `ReadingStatus`; contrato do repositório e exceção de livro inexistente.
- `application`: casos de uso de cadastrar, listar, consultar, atualizar e excluir, com objetos de entrada e saída.
- `infrastructure`: controller REST, validação das requisições, tratamento padronizado de erros, entidade JPA, mapeadores, adaptador do repositório e configuração da injeção de dependências.

O domínio valida suas regras independentemente do Spring. Os casos de uso dependem da interface `BookRepository`; o adaptador implementa essa interface utilizando Spring Data JPA.

## Banco de dados

O H2 grava os livros em `data/booksdb`, preservando os cadastros entre reinicializações. A pasta `data/` é ignorada pelo Git. O console local está em `http://localhost:8080/h2-console`, com URL JDBC `jdbc:h2:file:./data/booksdb`, usuário `sa` e senha vazia.
