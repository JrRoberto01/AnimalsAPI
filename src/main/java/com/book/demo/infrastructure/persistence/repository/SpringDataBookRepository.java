package com.book.demo.infrastructure.persistence.repository;

import com.book.demo.infrastructure.persistence.entity.BookJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SpringDataBookRepository extends JpaRepository<BookJpaEntity, UUID> {}
