package com.animal.demo.application.usecase;

import com.animal.demo.application.output.AnimalOutput;
import com.animal.demo.domain.repository.AnimalRepository;

import java.util.List;
import java.util.Objects;

public class ListAnimalsUseCase {

    private final AnimalRepository animalRepository;

    public ListAnimalsUseCase(AnimalRepository animalRepository) {
        this.animalRepository = Objects.requireNonNull(animalRepository, "animalRepository nao pode ser nulo");
    }

    public List<AnimalOutput> execute() {
        return animalRepository.findAll().stream()
                .map(AnimalOutput::from)
                .toList();
    }
}
