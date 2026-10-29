package com.book.demo.infrastructure.config;

import com.book.demo.application.usecase.CreateBookUseCase;
import com.book.demo.application.usecase.DeleteBookUseCase;
import com.book.demo.application.usecase.GetBookByIdUseCase;
import com.book.demo.application.usecase.ListBooksUseCase;
import com.book.demo.application.usecase.UpdateBookUseCase;
import com.book.demo.domain.repository.BookRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfiguration {
    @Bean public CreateBookUseCase createBookUseCase(BookRepository repository) { return new CreateBookUseCase(repository); }
    @Bean public GetBookByIdUseCase getBookByIdUseCase(BookRepository repository) { return new GetBookByIdUseCase(repository); }
    @Bean public ListBooksUseCase listBooksUseCase(BookRepository repository) { return new ListBooksUseCase(repository); }
    @Bean public UpdateBookUseCase updateBookUseCase(BookRepository repository) { return new UpdateBookUseCase(repository); }
    @Bean public DeleteBookUseCase deleteBookUseCase(BookRepository repository) { return new DeleteBookUseCase(repository); }
}
