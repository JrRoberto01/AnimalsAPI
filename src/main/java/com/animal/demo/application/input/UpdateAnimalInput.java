package com.animal.demo.application.input;

public record UpdateAnimalInput(
        String nome,
        String especie,
        Integer idade,
        String sexo,
        boolean disponivelAdocao
) {
}
