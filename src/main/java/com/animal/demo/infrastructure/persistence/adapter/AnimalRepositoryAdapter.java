package com.animal.demo.infrastructure.persistence.adapter;

import com.animal.demo.domain.model.Animal;
import com.animal.demo.domain.model.AnimalId;
import com.animal.demo.domain.repository.AnimalRepository;
import com.animal.demo.infrastructure.persistence.mapper.AnimalPersistenceMapper;
import com.animal.demo.infrastructure.persistence.repository.SpringDataAnimalRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class AnimalRepositoryAdapter implements AnimalRepository {
    private final SpringDataAnimalRepository repository;

    public AnimalRepositoryAdapter(SpringDataAnimalRepository repository) {
        this.repository = repository;
    }

    @Override
    public Animal save(Animal animal) {
        return AnimalPersistenceMapper.toDomain(repository.save(AnimalPersistenceMapper.toEntity(animal)));
    }

    @Override
    public Optional<Animal> findById(AnimalId id) {
        return repository.findById(id.id()).map(AnimalPersistenceMapper::toDomain);
    }

    @Override
    public List<Animal> findAll() {
        return repository.findAll().stream().map(AnimalPersistenceMapper::toDomain).toList();
    }

    @Override
    public void deleteById(AnimalId id) {
        repository.deleteById(id.id());
    }
}
