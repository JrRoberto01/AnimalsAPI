package com.book.demo.application.usecase;

import com.book.demo.application.input.CreateBookInput;
import com.book.demo.application.input.UpdateBookInput;
import com.book.demo.application.output.BookOutput;
import com.book.demo.domain.exception.BookNotFoundException;
import com.book.demo.domain.model.Book;
import static com.book.demo.domain.model.ReadingStatus.*;
import com.book.demo.domain.model.BookId;
import com.book.demo.domain.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BookUseCasesTest {

    private FakeBookRepository repository;

    @BeforeEach
    void setUp() {
        repository = new FakeBookRepository();
    }

    @Test
    void shouldCreateBook() {
        BookOutput output = new CreateBookUseCase(repository).execute(
                new CreateBookInput("Dom Casmurro", "Machado de Assis", 1899, "Romance", NAO_LIDO)
        );

        assertEquals("Dom Casmurro", output.titulo());
        assertEquals("Machado de Assis", output.autor());
        assertEquals(1899, output.anoPublicacao());
        assertEquals("Romance", output.genero());
        assertEquals(NAO_LIDO, output.statusLeitura());
        assertTrue(repository.findById(new BookId(output.id())).isPresent());
    }

    @Test
    void shouldGetBookById() {
        Book book = repository.save(new Book("1984", "George Orwell", 1949, "Distopia", LENDO));

        BookOutput output = new GetBookByIdUseCase(repository).execute(book.getId());

        assertEquals(book.getId().id(), output.id());
        assertEquals("1984", output.titulo());
    }

    @Test
    void shouldListBooks() {
        repository.save(new Book("Dom Casmurro", "Machado de Assis", 1899, "Romance", NAO_LIDO));
        repository.save(new Book("1984", "George Orwell", 1949, "Distopia", LIDO));

        List<BookOutput> books = new ListBooksUseCase(repository).execute();

        assertEquals(2, books.size());
        assertEquals(List.of("Dom Casmurro", "1984"), books.stream().map(BookOutput::titulo).toList());
    }

    @Test
    void shouldUpdateBook() {
        Book book = repository.save(new Book("O Hobbit", "J. R. R. Tolkien", 1937, "Fantasia", NAO_LIDO));

        BookOutput output = new UpdateBookUseCase(repository).execute(
                book.getId(),
                new UpdateBookInput("O Hobbit revisado", "Tolkien", 1938, "Aventura", LIDO)
        );

        assertEquals(1938, output.anoPublicacao());
        assertEquals(LIDO, output.statusLeitura());
        assertEquals(1938, repository.findById(book.getId()).orElseThrow().getAnoPublicacao());
    }

    @Test
    void shouldDeleteBook() {
        Book book = repository.save(new Book("Duna", "Frank Herbert", 1965, "Ficcao", LIDO));

        new DeleteBookUseCase(repository).execute(book.getId());

        assertTrue(repository.findById(book.getId()).isEmpty());
    }

    @Test
    void shouldThrowNotFoundWhenGettingUpdatingOrDeletingUnknownBook() {
        UUID unknownId = UUID.randomUUID();

        assertThrows(BookNotFoundException.class,
                () -> new GetBookByIdUseCase(repository).execute(unknownId));
        assertThrows(BookNotFoundException.class,
                () -> new UpdateBookUseCase(repository).execute(
                        unknownId,
                        new UpdateBookInput("Dom Casmurro", "Machado de Assis", 1899, "Romance", NAO_LIDO)
                ));
        assertThrows(BookNotFoundException.class,
                () -> new DeleteBookUseCase(repository).execute(unknownId));
    }

    private static class FakeBookRepository implements BookRepository {

        private final Map<BookId, Book> books = new LinkedHashMap<>();

        @Override
        public Book save(Book book) {
            books.put(book.getId(), book);
            return book;
        }

        @Override
        public Optional<Book> findById(BookId id) {
            return Optional.ofNullable(books.get(id));
        }

        @Override
        public List<Book> findAll() {
            return List.copyOf(books.values());
        }

        @Override
        public void deleteById(BookId id) {
            books.remove(id);
        }
    }
}
