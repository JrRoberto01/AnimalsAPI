package com.animal.demo.application.usecase;

import com.animal.demo.application.input.CreateAnimalInput;
import com.animal.demo.application.output.AnimalOutput;
import com.animal.demo.domain.model.Animal;
import com.animal.demo.domain.repository.AnimalRepository;

import java.util.Objects;

public class CreateAnimalUseCase {

    private final AnimalRepository animalRepository;

    public CreateAnimalUseCase(AnimalRepository animalRepository) {
        this.animalRepository = Objects.requireNonNull(animalRepository, "animalRepository nao pode ser nulo");
    }

    public AnimalOutput execute(CreateAnimalInput input) {
        Objects.requireNonNull(input, "input nao pode ser nulo");

        Animal animal = new Animal(
                input.nome(),
                input.especie(),
                input.idade(),
                input.sexo(),
                input.disponivelAdocao()
        );

        return AnimalOutput.from(animalRepository.save(animal));
    }
}
