package com.book.demo.application.usecase;

import com.book.demo.application.input.UpdateBookInput;
import com.book.demo.application.output.BookOutput;
import com.book.demo.domain.exception.BookNotFoundException;
import com.book.demo.domain.model.Book;
import com.book.demo.domain.model.BookId;
import com.book.demo.domain.repository.BookRepository;

import java.util.Objects;
import java.util.UUID;

public class UpdateBookUseCase {

    private final BookRepository bookRepository;

    public UpdateBookUseCase(BookRepository bookRepository) {
        this.bookRepository = Objects.requireNonNull(bookRepository, "bookRepository nao pode ser nulo");
    }

    public BookOutput execute(UUID id, UpdateBookInput input) {
        return execute(new BookId(id), input);
    }

    public BookOutput execute(BookId id, UpdateBookInput input) {
        Objects.requireNonNull(id, "id nao pode ser nulo");
        Objects.requireNonNull(input, "input nao pode ser nulo");

        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException(id));

        book.update(
                input.titulo(),
                input.autor(),
                input.anoPublicacao(),
                input.genero(),
                input.statusLeitura()
        );

        return BookOutput.from(bookRepository.save(book));
    }
}
