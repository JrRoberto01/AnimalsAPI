package com.animal.demo.application.usecase;

import com.animal.demo.application.output.AnimalOutput;
import com.animal.demo.domain.exception.AnimalNotFoundException;
import com.animal.demo.domain.model.AnimalId;
import com.animal.demo.domain.repository.AnimalRepository;

import java.util.Objects;
import java.util.UUID;

public class GetAnimalByIdUseCase {

    private final AnimalRepository animalRepository;

    public GetAnimalByIdUseCase(AnimalRepository animalRepository) {
        this.animalRepository = Objects.requireNonNull(animalRepository, "animalRepository nao pode ser nulo");
    }

    public AnimalOutput execute(UUID id) {
        return execute(new AnimalId(id));
    }

    public AnimalOutput execute(AnimalId id) {
        Objects.requireNonNull(id, "id nao pode ser nulo");

        return animalRepository.findById(id)
                .map(AnimalOutput::from)
                .orElseThrow(() -> new AnimalNotFoundException(id));
    }
}
