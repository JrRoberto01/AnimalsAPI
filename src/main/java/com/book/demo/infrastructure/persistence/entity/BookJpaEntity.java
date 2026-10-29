package com.book.demo.infrastructure.persistence.entity;

import com.book.demo.domain.model.ReadingStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.UUID;

@Entity
@Table(name = "books")
public class BookJpaEntity {
    @Id private UUID id;
    @Column(nullable = false, length = 200) private String titulo;
    @Column(nullable = false, length = 150) private String autor;
    @Column(nullable = false) private Integer anoPublicacao;
    @Column(nullable = false, length = 80) private String genero;
    @Enumerated(EnumType.STRING)
    @Column(name = "status_leitura", nullable = false, length = 20) private ReadingStatus statusLeitura;

    protected BookJpaEntity() {}

    public BookJpaEntity(UUID id, String titulo, String autor, Integer anoPublicacao,
                           String genero, ReadingStatus statusLeitura) {
        this.id = id;
        this.titulo = titulo;
        this.autor = autor;
        this.anoPublicacao = anoPublicacao;
        this.genero = genero;
        this.statusLeitura = statusLeitura;
    }

    public UUID getId() { return id; }
    public String getTitulo() { return titulo; }
    public String getAutor() { return autor; }
    public Integer getAnoPublicacao() { return anoPublicacao; }
    public String getGenero() { return genero; }
    public ReadingStatus getStatusLeitura() { return statusLeitura; }
}
