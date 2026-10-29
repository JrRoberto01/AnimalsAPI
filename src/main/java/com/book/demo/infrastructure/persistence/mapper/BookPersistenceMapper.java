package com.book.demo.infrastructure.persistence.mapper;

import com.book.demo.domain.model.Book;
import com.book.demo.domain.model.BookId;
import com.book.demo.infrastructure.persistence.entity.BookJpaEntity;

public final class BookPersistenceMapper {
    private BookPersistenceMapper() {}

    public static BookJpaEntity toEntity(Book book) {
        return new BookJpaEntity(book.getId().id(), book.getTitulo(), book.getAutor(),
                book.getAnoPublicacao(), book.getGenero(), book.getStatusLeitura());
    }

    public static Book toDomain(BookJpaEntity entity) {
        return new Book(new BookId(entity.getId()), entity.getTitulo(), entity.getAutor(),
                entity.getAnoPublicacao(), entity.getGenero(), entity.getStatusLeitura());
    }
}
