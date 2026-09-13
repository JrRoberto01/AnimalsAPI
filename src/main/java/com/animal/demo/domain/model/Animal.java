package com.animal.demo.domain.model;

public class Animal {

    private final AnimalId id;
    private String nome;
    private String especie;
    private Integer idade;
    private String sexo;
    private boolean disponivelAdocao;

    public Animal(
            String nome,
            String especie,
            Integer idade,
            String sexo,
            boolean disponivelAdocao
    ) {
        this(new AnimalId(), nome, especie, idade, sexo, disponivelAdocao);
    }

    public Animal(
            AnimalId id,
            String nome,
            String especie,
            Integer idade,
            String sexo,
            boolean disponivelAdocao
    ) {
        if (id == null) {
            throw new IllegalArgumentException("O identificador do animal nao pode ser nulo");
        }

        validate(nome, especie, idade, sexo);

        this.id = id;
        this.nome = nome.trim();
        this.especie = especie.trim();
        this.idade = idade;
        this.sexo = sexo.trim();
        this.disponivelAdocao = disponivelAdocao;
    }

    public void update(
            String nome,
            String especie,
            Integer idade,
            String sexo,
            boolean disponivelAdocao
    ) {
        validate(nome, especie, idade, sexo);

        this.nome = nome.trim();
        this.especie = especie.trim();
        this.idade = idade;
        this.sexo = sexo.trim();
        this.disponivelAdocao = disponivelAdocao;
    }

    public AnimalId getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getEspecie() {
        return especie;
    }

    public Integer getIdade() {
        return idade;
    }

    public String getSexo() {
        return sexo;
    }

    public boolean isDisponivelAdocao() {
        return disponivelAdocao;
    }

    private static void validate(String nome, String especie, Integer idade, String sexo) {
        validateText(nome, "O nome do animal nao pode estar vazio");
        if (nome.trim().length() < 2 || nome.trim().length() > 100) {
            throw new IllegalArgumentException("O nome do animal deve possuir entre 2 e 100 caracteres");
        }

        validateText(especie, "A especie do animal nao pode estar vazia");
        if (especie.trim().length() > 50) {
            throw new IllegalArgumentException("A especie do animal deve possuir no maximo 50 caracteres");
        }

        if (idade == null) {
            throw new IllegalArgumentException("A idade do animal nao pode ser nula");
        }
        if (idade < 0) {
            throw new IllegalArgumentException("A idade do animal nao pode ser negativa");
        }

        validateText(sexo, "O sexo do animal nao pode estar vazio");
        if (sexo.trim().length() > 30) {
            throw new IllegalArgumentException("O sexo do animal deve possuir no maximo 30 caracteres");
        }
    }

    private static void validateText(String value, String message) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(message);
        }
    }
}
