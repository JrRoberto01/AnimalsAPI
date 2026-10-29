package com.book.demo.application.usecase;

import com.book.demo.application.output.BookOutput;
import com.book.demo.domain.exception.BookNotFoundException;
import com.book.demo.domain.model.BookId;
import com.book.demo.domain.repository.BookRepository;

import java.util.Objects;
import java.util.UUID;

public class GetBookByIdUseCase {

    private final BookRepository bookRepository;

    public GetBookByIdUseCase(BookRepository bookRepository) {
        this.bookRepository = Objects.requireNonNull(bookRepository, "bookRepository nao pode ser nulo");
    }

    public BookOutput execute(UUID id) {
        return execute(new BookId(id));
    }

    public BookOutput execute(BookId id) {
        Objects.requireNonNull(id, "id nao pode ser nulo");

        return bookRepository.findById(id)
                .map(BookOutput::from)
                .orElseThrow(() -> new BookNotFoundException(id));
    }
}
