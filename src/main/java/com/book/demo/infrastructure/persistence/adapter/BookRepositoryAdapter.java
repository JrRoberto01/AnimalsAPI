package com.book.demo.infrastructure.persistence.adapter;

import com.book.demo.domain.model.Book;
import com.book.demo.domain.model.BookId;
import com.book.demo.domain.repository.BookRepository;
import com.book.demo.infrastructure.persistence.mapper.BookPersistenceMapper;
import com.book.demo.infrastructure.persistence.repository.SpringDataBookRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class BookRepositoryAdapter implements BookRepository {
    private final SpringDataBookRepository repository;

    public BookRepositoryAdapter(SpringDataBookRepository repository) {
        this.repository = repository;
    }

    @Override
    public Book save(Book book) {
        return BookPersistenceMapper.toDomain(repository.save(BookPersistenceMapper.toEntity(book)));
    }

    @Override
    public Optional<Book> findById(BookId id) {
        return repository.findById(id.id()).map(BookPersistenceMapper::toDomain);
    }

    @Override
    public List<Book> findAll() {
        return repository.findAll().stream().map(BookPersistenceMapper::toDomain).toList();
    }

    @Override
    public void deleteById(BookId id) {
        repository.deleteById(id.id());
    }
}
