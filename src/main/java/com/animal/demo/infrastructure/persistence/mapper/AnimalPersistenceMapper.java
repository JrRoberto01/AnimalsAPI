package com.animal.demo.infrastructure.persistence.mapper;

import com.animal.demo.domain.model.Animal;
import com.animal.demo.domain.model.AnimalId;
import com.animal.demo.infrastructure.persistence.entity.AnimalJpaEntity;

public final class AnimalPersistenceMapper {
    private AnimalPersistenceMapper() {}

    public static AnimalJpaEntity toEntity(Animal animal) {
        return new AnimalJpaEntity(animal.getId().id(), animal.getNome(), animal.getEspecie(),
                animal.getIdade(), animal.getSexo(), animal.isDisponivelAdocao());
    }

    public static Animal toDomain(AnimalJpaEntity entity) {
        return new Animal(new AnimalId(entity.getId()), entity.getNome(), entity.getEspecie(),
                entity.getIdade(), entity.getSexo(), entity.isDisponivelAdocao());
    }
}
