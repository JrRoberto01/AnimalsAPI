package com.book.demo.application.output;

import com.book.demo.domain.model.ReadingStatus;

import com.book.demo.domain.model.Book;

import java.util.UUID;

public record BookOutput(
        UUID id,
        String titulo,
        String autor,
        Integer anoPublicacao,
        String genero,
        ReadingStatus statusLeitura
) {
    public static BookOutput from(Book book) {
        return new BookOutput(
                book.getId().id(),
                book.getTitulo(),
                book.getAutor(),
                book.getAnoPublicacao(),
                book.getGenero(),
                book.getStatusLeitura()
        );
    }
}
