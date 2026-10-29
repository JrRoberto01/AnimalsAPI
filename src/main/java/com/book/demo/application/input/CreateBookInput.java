package com.book.demo.application.input;

import com.book.demo.domain.model.ReadingStatus;

public record CreateBookInput(
        String titulo,
        String autor,
        Integer anoPublicacao,
        String genero,
        ReadingStatus statusLeitura
) {
}
