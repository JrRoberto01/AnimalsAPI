package com.book.demo.domain.repository;

import com.book.demo.domain.model.Book;
import com.book.demo.domain.model.BookId;

import java.util.List;
import java.util.Optional;

public interface BookRepository {

    Book save(Book book);

    Optional<Book> findById(BookId id);

    List<Book> findAll();

    void deleteById(BookId id);
}
