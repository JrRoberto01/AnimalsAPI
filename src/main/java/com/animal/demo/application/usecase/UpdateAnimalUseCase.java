package com.animal.demo.application.usecase;

import com.animal.demo.application.input.UpdateAnimalInput;
import com.animal.demo.application.output.AnimalOutput;
import com.animal.demo.domain.exception.AnimalNotFoundException;
import com.animal.demo.domain.model.Animal;
import com.animal.demo.domain.model.AnimalId;
import com.animal.demo.domain.repository.AnimalRepository;

import java.util.Objects;
import java.util.UUID;

public class UpdateAnimalUseCase {

    private final AnimalRepository animalRepository;

    public UpdateAnimalUseCase(AnimalRepository animalRepository) {
        this.animalRepository = Objects.requireNonNull(animalRepository, "animalRepository nao pode ser nulo");
    }

    public AnimalOutput execute(UUID id, UpdateAnimalInput input) {
        return execute(new AnimalId(id), input);
    }

    public AnimalOutput execute(AnimalId id, UpdateAnimalInput input) {
        Objects.requireNonNull(id, "id nao pode ser nulo");
        Objects.requireNonNull(input, "input nao pode ser nulo");

        Animal animal = animalRepository.findById(id)
                .orElseThrow(() -> new AnimalNotFoundException(id));

        animal.update(
                input.nome(),
                input.especie(),
                input.idade(),
                input.sexo(),
                input.disponivelAdocao()
        );

        return AnimalOutput.from(animalRepository.save(animal));
    }
}
