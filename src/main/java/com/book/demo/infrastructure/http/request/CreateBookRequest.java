package com.book.demo.infrastructure.http.request;

import com.book.demo.domain.model.ReadingStatus;

import com.book.demo.application.input.CreateBookInput;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record CreateBookRequest(
        @NotBlank @Size(max = 200) String titulo,
        @NotBlank @Size(max = 150) String autor,
        @NotNull @Positive Integer anoPublicacao,
        @NotBlank @Size(max = 80) String genero,
        @NotNull ReadingStatus statusLeitura
) {
    public CreateBookInput toInput() {
        return new CreateBookInput(titulo, autor, anoPublicacao, genero, statusLeitura);
    }
}
