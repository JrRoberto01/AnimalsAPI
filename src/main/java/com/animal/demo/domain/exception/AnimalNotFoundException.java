package com.animal.demo.domain.exception;

import com.animal.demo.domain.model.AnimalId;

public class AnimalNotFoundException extends RuntimeException {

    private final AnimalId animalId;

    public AnimalNotFoundException(AnimalId animalId) {
        super(buildMessage(animalId));
        this.animalId = animalId;
    }

    public AnimalId getAnimalId() {
        return animalId;
    }

    private static String buildMessage(AnimalId animalId) {
        if (animalId == null) {
            throw new IllegalArgumentException("O identificador do animal não pode ser nulo");
        }

        return "Animal com identificador " + animalId.id() + " não encontrado";
    }
}
