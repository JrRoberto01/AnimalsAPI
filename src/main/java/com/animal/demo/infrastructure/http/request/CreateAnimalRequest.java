package com.animal.demo.infrastructure.http.request;

import com.animal.demo.application.input.CreateAnimalInput;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

public record CreateAnimalRequest(
        @NotBlank @Size(min = 2, max = 100) String nome,
        @NotBlank @Size(max = 50) String especie,
        @NotNull @PositiveOrZero Integer idade,
        @NotBlank @Size(max = 30) String sexo,
        boolean disponivelAdocao
) {
    public CreateAnimalInput toInput() {
        return new CreateAnimalInput(nome, especie, idade, sexo, disponivelAdocao);
    }
}
