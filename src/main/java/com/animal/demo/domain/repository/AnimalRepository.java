package com.animal.demo.domain.repository;

import com.animal.demo.domain.model.Animal;
import com.animal.demo.domain.model.AnimalId;

import java.util.List;
import java.util.Optional;

public interface AnimalRepository {

    Animal save(Animal animal);

    Optional<Animal> findById(AnimalId id);

    List<Animal> findAll();

    void deleteById(AnimalId id);
}
