package com.animal.demo.application.input;

public record CreateAnimalInput(
        String nome,
        String especie,
        Integer idade,
        String sexo,
        boolean disponivelAdocao
) {
}
