package com.animal.demo.application.usecase;

import com.animal.demo.domain.exception.AnimalNotFoundException;
import com.animal.demo.domain.model.AnimalId;
import com.animal.demo.domain.repository.AnimalRepository;

import java.util.Objects;
import java.util.UUID;

public class DeleteAnimalUseCase {

    private final AnimalRepository animalRepository;

    public DeleteAnimalUseCase(AnimalRepository animalRepository) {
        this.animalRepository = Objects.requireNonNull(animalRepository, "animalRepository nao pode ser nulo");
    }

    public void execute(UUID id) {
        execute(new AnimalId(id));
    }

    public void execute(AnimalId id) {
        Objects.requireNonNull(id, "id nao pode ser nulo");

        if (animalRepository.findById(id).isEmpty()) {
            throw new AnimalNotFoundException(id);
        }

        animalRepository.deleteById(id);
    }
}
