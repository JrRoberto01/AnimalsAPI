package com.animal.demo.domain.model;

import java.util.UUID;

public record AnimalId(UUID id) {

    public AnimalId {
        if (id == null) {
            throw new IllegalArgumentException("O identificador do animal nao pode ser nulo");
        }
    }

    public AnimalId() {
        this(UUID.randomUUID());
    }

    public static AnimalId from(String id) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("O identificador do animal nao pode estar vazio");
        }

        try {
            return new AnimalId(UUID.fromString(id.trim()));
        } catch (IllegalArgumentException exception) {
            throw new IllegalArgumentException("O identificador do animal deve ser um UUID valido", exception);
        }
    }

    @Override
    public String toString() {
        return id.toString();
    }
}
