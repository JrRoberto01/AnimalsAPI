package com.book.demo.domain.model;

import java.time.Year;

public class Book {

    private final BookId id;
    private String titulo;
    private String autor;
    private Integer anoPublicacao;
    private String genero;
    private ReadingStatus statusLeitura;

    public Book(
            String titulo,
            String autor,
            Integer anoPublicacao,
            String genero,
            ReadingStatus statusLeitura
    ) {
        this(new BookId(), titulo, autor, anoPublicacao, genero, statusLeitura);
    }

    public Book(
            BookId id,
            String titulo,
            String autor,
            Integer anoPublicacao,
            String genero,
            ReadingStatus statusLeitura
    ) {
        if (id == null) {
            throw new IllegalArgumentException("O identificador do livro nao pode ser nulo");
        }

        validate(titulo, autor, anoPublicacao, genero, statusLeitura);

        this.id = id;
        this.titulo = titulo.trim();
        this.autor = autor.trim();
        this.anoPublicacao = anoPublicacao;
        this.genero = genero.trim();
        this.statusLeitura = statusLeitura;
    }

    public void update(
            String titulo,
            String autor,
            Integer anoPublicacao,
            String genero,
            ReadingStatus statusLeitura
    ) {
        validate(titulo, autor, anoPublicacao, genero, statusLeitura);

        this.titulo = titulo.trim();
        this.autor = autor.trim();
        this.anoPublicacao = anoPublicacao;
        this.genero = genero.trim();
        this.statusLeitura = statusLeitura;
    }

    public BookId getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getAutor() {
        return autor;
    }

    public Integer getAnoPublicacao() {
        return anoPublicacao;
    }

    public String getGenero() {
        return genero;
    }

    public ReadingStatus getStatusLeitura() {
        return statusLeitura;
    }

    private static void validate(String titulo, String autor, Integer anoPublicacao, String genero,
                                 ReadingStatus statusLeitura) {
        validateText(titulo, "O titulo do livro nao pode estar vazio");
        if (titulo.trim().length() > 200) {
            throw new IllegalArgumentException("O titulo do livro deve possuir no maximo 200 caracteres");
        }

        validateText(autor, "O autor do livro nao pode estar vazio");
        if (autor.trim().length() > 150) {
            throw new IllegalArgumentException("O autor do livro deve possuir no maximo 150 caracteres");
        }

        if (anoPublicacao == null) {
            throw new IllegalArgumentException("O ano de publicacao nao pode ser nulo");
        }
        if (anoPublicacao < 1 || anoPublicacao > Year.now().getValue()) {
            throw new IllegalArgumentException("O ano de publicacao deve estar entre 1 e o ano atual");
        }

        validateText(genero, "O genero do livro nao pode estar vazio");
        if (genero.trim().length() > 80) {
            throw new IllegalArgumentException("O genero do livro deve possuir no maximo 80 caracteres");
        }
        if (statusLeitura == null) {
            throw new IllegalArgumentException("O status de leitura nao pode ser nulo");
        }
    }

    private static void validateText(String value, String message) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(message);
        }
    }
}
