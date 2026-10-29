package com.book.demo.infrastructure.http.response;

import com.book.demo.domain.model.ReadingStatus;

import com.book.demo.application.output.BookOutput;

import java.util.UUID;

public record BookResponse(
        UUID id, String titulo, String autor, Integer anoPublicacao, String genero, ReadingStatus statusLeitura
) {
    public static BookResponse from(BookOutput output) {
        return new BookResponse(output.id(), output.titulo(), output.autor(), output.anoPublicacao(),
                output.genero(), output.statusLeitura());
    }
}
