package com.animal.demo.infrastructure.http.controller;

import com.animal.demo.application.usecase.CreateAnimalUseCase;
import com.animal.demo.application.usecase.DeleteAnimalUseCase;
import com.animal.demo.application.usecase.GetAnimalByIdUseCase;
import com.animal.demo.application.usecase.ListAnimalsUseCase;
import com.animal.demo.application.usecase.UpdateAnimalUseCase;
import com.animal.demo.infrastructure.http.request.CreateAnimalRequest;
import com.animal.demo.infrastructure.http.request.UpdateAnimalRequest;
import com.animal.demo.infrastructure.http.response.AnimalResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/animals")
public class AnimalController {
    private final CreateAnimalUseCase create;
    private final GetAnimalByIdUseCase getById;
    private final ListAnimalsUseCase list;
    private final UpdateAnimalUseCase update;
    private final DeleteAnimalUseCase delete;

    public AnimalController(CreateAnimalUseCase create, GetAnimalByIdUseCase getById,
                            ListAnimalsUseCase list, UpdateAnimalUseCase update,
                            DeleteAnimalUseCase delete) {
        this.create = create;
        this.getById = getById;
        this.list = list;
        this.update = update;
        this.delete = delete;
    }

    @PostMapping
    public ResponseEntity<AnimalResponse> create(@Valid @RequestBody CreateAnimalRequest request) {
        AnimalResponse response = AnimalResponse.from(create.execute(request.toInput()));
        return ResponseEntity.created(URI.create("/animals/" + response.id())).body(response);
    }

    @GetMapping
    public List<AnimalResponse> list() {
        return list.execute().stream().map(AnimalResponse::from).toList();
    }

    @GetMapping("/{id}")
    public AnimalResponse getById(@PathVariable UUID id) {
        return AnimalResponse.from(getById.execute(id));
    }

    @PutMapping("/{id}")
    public AnimalResponse update(@PathVariable UUID id, @Valid @RequestBody UpdateAnimalRequest request) {
        return AnimalResponse.from(update.execute(id, request.toInput()));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id) {
        delete.execute(id);
    }
}
