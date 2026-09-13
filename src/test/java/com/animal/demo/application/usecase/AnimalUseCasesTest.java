package com.animal.demo.application.usecase;

import com.animal.demo.application.input.CreateAnimalInput;
import com.animal.demo.application.input.UpdateAnimalInput;
import com.animal.demo.application.output.AnimalOutput;
import com.animal.demo.domain.exception.AnimalNotFoundException;
import com.animal.demo.domain.model.Animal;
import com.animal.demo.domain.model.AnimalId;
import com.animal.demo.domain.repository.AnimalRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AnimalUseCasesTest {

    private FakeAnimalRepository repository;

    @BeforeEach
    void setUp() {
        repository = new FakeAnimalRepository();
    }

    @Test
    void shouldCreateAnimal() {
        AnimalOutput output = new CreateAnimalUseCase(repository).execute(
                new CreateAnimalInput("Luna", "Gato", 2, "Femea", true)
        );

        assertEquals("Luna", output.nome());
        assertEquals("Gato", output.especie());
        assertEquals(2, output.idade());
        assertEquals("Femea", output.sexo());
        assertTrue(output.disponivelAdocao());
        assertTrue(repository.findById(new AnimalId(output.id())).isPresent());
    }

    @Test
    void shouldGetAnimalById() {
        Animal animal = repository.save(new Animal("Thor", "Cachorro", 4, "Macho", true));

        AnimalOutput output = new GetAnimalByIdUseCase(repository).execute(animal.getId());

        assertEquals(animal.getId().id(), output.id());
        assertEquals("Thor", output.nome());
    }

    @Test
    void shouldListAnimals() {
        repository.save(new Animal("Luna", "Gato", 2, "Femea", true));
        repository.save(new Animal("Thor", "Cachorro", 4, "Macho", false));

        List<AnimalOutput> animals = new ListAnimalsUseCase(repository).execute();

        assertEquals(2, animals.size());
        assertEquals(List.of("Luna", "Thor"), animals.stream().map(AnimalOutput::nome).toList());
    }

    @Test
    void shouldUpdateAnimal() {
        Animal animal = repository.save(new Animal("Toto", "Cachorro", 3, "Macho", true));

        AnimalOutput output = new UpdateAnimalUseCase(repository).execute(
                animal.getId(),
                new UpdateAnimalInput("Toto", "Cachorro", 4, "Macho", false)
        );

        assertEquals(4, output.idade());
        assertFalse(output.disponivelAdocao());
        assertEquals(4, repository.findById(animal.getId()).orElseThrow().getIdade());
    }

    @Test
    void shouldDeleteAnimal() {
        Animal animal = repository.save(new Animal("Nina", "Gato", 1, "Femea", true));

        new DeleteAnimalUseCase(repository).execute(animal.getId());

        assertTrue(repository.findById(animal.getId()).isEmpty());
    }

    @Test
    void shouldThrowNotFoundWhenGettingUpdatingOrDeletingUnknownAnimal() {
        UUID unknownId = UUID.randomUUID();

        assertThrows(AnimalNotFoundException.class,
                () -> new GetAnimalByIdUseCase(repository).execute(unknownId));
        assertThrows(AnimalNotFoundException.class,
                () -> new UpdateAnimalUseCase(repository).execute(
                        unknownId,
                        new UpdateAnimalInput("Luna", "Gato", 2, "Femea", true)
                ));
        assertThrows(AnimalNotFoundException.class,
                () -> new DeleteAnimalUseCase(repository).execute(unknownId));
    }

    private static class FakeAnimalRepository implements AnimalRepository {

        private final Map<AnimalId, Animal> animals = new LinkedHashMap<>();

        @Override
        public Animal save(Animal animal) {
            animals.put(animal.getId(), animal);
            return animal;
        }

        @Override
        public Optional<Animal> findById(AnimalId id) {
            return Optional.ofNullable(animals.get(id));
        }

        @Override
        public List<Animal> findAll() {
            return List.copyOf(animals.values());
        }

        @Override
        public void deleteById(AnimalId id) {
            animals.remove(id);
        }
    }
}
