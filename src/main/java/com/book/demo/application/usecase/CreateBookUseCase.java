package com.book.demo.application.usecase;

import com.book.demo.application.input.CreateBookInput;
import com.book.demo.application.output.BookOutput;
import com.book.demo.domain.model.Book;
import com.book.demo.domain.repository.BookRepository;

import java.util.Objects;

public class CreateBookUseCase {

    private final BookRepository bookRepository;

    public CreateBookUseCase(BookRepository bookRepository) {
        this.bookRepository = Objects.requireNonNull(bookRepository, "bookRepository nao pode ser nulo");
    }

    public BookOutput execute(CreateBookInput input) {
        Objects.requireNonNull(input, "input nao pode ser nulo");

        Book book = new Book(
                input.titulo(),
                input.autor(),
                input.anoPublicacao(),
                input.genero(),
                input.statusLeitura()
        );

        return BookOutput.from(bookRepository.save(book));
    }
}
