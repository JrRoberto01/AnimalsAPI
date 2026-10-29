package com.book.demo.infrastructure.http.controller;

import com.book.demo.application.usecase.CreateBookUseCase;
import com.book.demo.application.usecase.DeleteBookUseCase;
import com.book.demo.application.usecase.GetBookByIdUseCase;
import com.book.demo.application.usecase.ListBooksUseCase;
import com.book.demo.application.usecase.UpdateBookUseCase;
import com.book.demo.infrastructure.http.request.CreateBookRequest;
import com.book.demo.infrastructure.http.request.UpdateBookRequest;
import com.book.demo.infrastructure.http.response.BookResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/books")
public class BookController {
    private final CreateBookUseCase create;
    private final GetBookByIdUseCase getById;
    private final ListBooksUseCase list;
    private final UpdateBookUseCase update;
    private final DeleteBookUseCase delete;

    public BookController(CreateBookUseCase create, GetBookByIdUseCase getById,
                            ListBooksUseCase list, UpdateBookUseCase update,
                            DeleteBookUseCase delete) {
        this.create = create;
        this.getById = getById;
        this.list = list;
        this.update = update;
        this.delete = delete;
    }

    @PostMapping
    public ResponseEntity<BookResponse> create(@Valid @RequestBody CreateBookRequest request) {
        BookResponse response = BookResponse.from(create.execute(request.toInput()));
        return ResponseEntity.created(URI.create("/books/" + response.id())).body(response);
    }

    @GetMapping
    public List<BookResponse> list() {
        return list.execute().stream().map(BookResponse::from).toList();
    }

    @GetMapping("/{id}")
    public BookResponse getById(@PathVariable UUID id) {
        return BookResponse.from(getById.execute(id));
    }

    @PutMapping("/{id}")
    public BookResponse update(@PathVariable UUID id, @Valid @RequestBody UpdateBookRequest request) {
        return BookResponse.from(update.execute(id, request.toInput()));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id) {
        delete.execute(id);
    }
}
