package com.book.demo.domain.model;

import org.junit.jupiter.api.Test;

import java.time.Year;

import static org.junit.jupiter.api.Assertions.*;

class BookTest {
    @Test
    void shouldTrimTextAndGenerateIdentity() {
        Book book = new Book(" Duna ", " Frank Herbert ", 1965, " Ficcao ", ReadingStatus.LENDO);
        assertNotNull(book.getId().id());
        assertEquals("Duna", book.getTitulo());
        assertEquals("Frank Herbert", book.getAutor());
        assertEquals("Ficcao", book.getGenero());
        assertNotEquals(book.getId(), new BookId());
    }

    @Test
    void shouldRejectInvalidDomainDataWithoutHttpValidation() {
        assertAll(
                () -> assertThrows(IllegalArgumentException.class,
                        () -> new Book(null, "Autor", 2000, "Genero", ReadingStatus.LIDO)),
                () -> assertThrows(IllegalArgumentException.class,
                        () -> new Book("Titulo", " ", 2000, "Genero", ReadingStatus.LIDO)),
                () -> assertThrows(IllegalArgumentException.class,
                        () -> new Book("Titulo", "Autor", 2000, "", ReadingStatus.LIDO)),
                () -> assertThrows(IllegalArgumentException.class,
                        () -> new Book("Titulo", "Autor", null, "Genero", ReadingStatus.LIDO)),
                () -> assertThrows(IllegalArgumentException.class,
                        () -> new Book("Titulo", "Autor", 0, "Genero", ReadingStatus.LIDO)),
                () -> assertThrows(IllegalArgumentException.class,
                        () -> new Book("Titulo", "Autor", Year.now().getValue() + 1, "Genero", ReadingStatus.LIDO)),
                () -> assertThrows(IllegalArgumentException.class,
                        () -> new Book("Titulo", "Autor", 2000, "Genero", null)),
                () -> assertThrows(IllegalArgumentException.class,
                        () -> new Book("T".repeat(201), "Autor", 2000, "Genero", ReadingStatus.LIDO)),
                () -> assertThrows(IllegalArgumentException.class,
                        () -> new Book("Titulo", "A".repeat(151), 2000, "Genero", ReadingStatus.LIDO)),
                () -> assertThrows(IllegalArgumentException.class,
                        () -> new Book("Titulo", "Autor", 2000, "G".repeat(81), ReadingStatus.LIDO))
        );
    }

    @Test
    void shouldAcceptBoundaryValues() {
        Book book = new Book("T".repeat(200), "A".repeat(150), 1, "G".repeat(80), ReadingStatus.NAO_LIDO);
        book.update("A", "B", Year.now().getValue(), "C", ReadingStatus.LIDO);
        assertEquals(Year.now().getValue(), book.getAnoPublicacao());
        assertEquals(ReadingStatus.LIDO, book.getStatusLeitura());
    }

    @Test
    void shouldKeepIdentityAndFieldsAfterInvalidDomainUpdate() {
        Book book = new Book("Duna", "Frank Herbert", 1965, "Ficcao", ReadingStatus.LENDO);
        BookId id = book.getId();
        assertThrows(IllegalArgumentException.class,
                () -> book.update("Outro", "Autor", 2000, "Genero", null));
        assertEquals(id, book.getId());
        assertEquals("Duna", book.getTitulo());
        assertEquals(ReadingStatus.LENDO, book.getStatusLeitura());
    }
}
