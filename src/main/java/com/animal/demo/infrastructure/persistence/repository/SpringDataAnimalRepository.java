package com.animal.demo.infrastructure.persistence.repository;

import com.animal.demo.infrastructure.persistence.entity.AnimalJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SpringDataAnimalRepository extends JpaRepository<AnimalJpaEntity, UUID> {}
