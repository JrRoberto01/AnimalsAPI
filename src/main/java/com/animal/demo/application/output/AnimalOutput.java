package com.animal.demo.application.output;

import com.animal.demo.domain.model.Animal;

import java.util.UUID;

public record AnimalOutput(
        UUID id,
        String nome,
        String especie,
        Integer idade,
        String sexo,
        boolean disponivelAdocao
) {
    public static AnimalOutput from(Animal animal) {
        return new AnimalOutput(
                animal.getId().id(),
                animal.getNome(),
                animal.getEspecie(),
                animal.getIdade(),
                animal.getSexo(),
                animal.isDisponivelAdocao()
        );
    }
}
