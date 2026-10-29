package com.book.demo.application.usecase;

import com.book.demo.domain.exception.BookNotFoundException;
import com.book.demo.domain.model.BookId;
import com.book.demo.domain.repository.BookRepository;

import java.util.Objects;
import java.util.UUID;

public class DeleteBookUseCase {

    private final BookRepository bookRepository;

    public DeleteBookUseCase(BookRepository bookRepository) {
        this.bookRepository = Objects.requireNonNull(bookRepository, "bookRepository nao pode ser nulo");
    }

    public void execute(UUID id) {
        execute(new BookId(id));
    }

    public void execute(BookId id) {
        Objects.requireNonNull(id, "id nao pode ser nulo");

        if (bookRepository.findById(id).isEmpty()) {
            throw new BookNotFoundException(id);
        }

        bookRepository.deleteById(id);
    }
}
