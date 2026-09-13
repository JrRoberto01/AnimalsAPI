package com.animal.demo.infrastructure.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.UUID;

@Entity
@Table(name = "animals")
public class AnimalJpaEntity {
    @Id private UUID id;
    @Column(nullable = false, length = 100) private String nome;
    @Column(nullable = false, length = 50) private String especie;
    @Column(nullable = false) private Integer idade;
    @Column(nullable = false, length = 30) private String sexo;
    @Column(name = "disponivel_adocao", nullable = false) private boolean disponivelAdocao;

    protected AnimalJpaEntity() {}

    public AnimalJpaEntity(UUID id, String nome, String especie, Integer idade,
                           String sexo, boolean disponivelAdocao) {
        this.id = id;
        this.nome = nome;
        this.especie = especie;
        this.idade = idade;
        this.sexo = sexo;
        this.disponivelAdocao = disponivelAdocao;
    }

    public UUID getId() { return id; }
    public String getNome() { return nome; }
    public String getEspecie() { return especie; }
    public Integer getIdade() { return idade; }
    public String getSexo() { return sexo; }
    public boolean isDisponivelAdocao() { return disponivelAdocao; }
}
