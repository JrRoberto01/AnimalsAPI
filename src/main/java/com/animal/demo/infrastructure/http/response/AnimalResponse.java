package com.animal.demo.infrastructure.http.response;

import com.animal.demo.application.output.AnimalOutput;

import java.util.UUID;

public record AnimalResponse(
        UUID id, String nome, String especie, Integer idade, String sexo, boolean disponivelAdocao
) {
    public static AnimalResponse from(AnimalOutput output) {
        return new AnimalResponse(output.id(), output.nome(), output.especie(), output.idade(),
                output.sexo(), output.disponivelAdocao());
    }
}
