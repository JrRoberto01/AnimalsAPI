package com.book.demo.domain.model;

import java.util.UUID;

public record BookId(UUID id) {

    public BookId {
        if (id == null) {
            throw new IllegalArgumentException("O identificador do livro nao pode ser nulo");
        }
    }

    public BookId() {
        this(UUID.randomUUID());
    }

    public static BookId from(String id) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("O identificador do livro nao pode estar vazio");
        }

        try {
            return new BookId(UUID.fromString(id.trim()));
        } catch (IllegalArgumentException exception) {
            throw new IllegalArgumentException("O identificador do livro deve ser um UUID valido", exception);
        }
    }

    @Override
    public String toString() {
        return id.toString();
    }
}
