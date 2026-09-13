package com.animal.demo.infrastructure.config;

import com.animal.demo.application.usecase.CreateAnimalUseCase;
import com.animal.demo.application.usecase.DeleteAnimalUseCase;
import com.animal.demo.application.usecase.GetAnimalByIdUseCase;
import com.animal.demo.application.usecase.ListAnimalsUseCase;
import com.animal.demo.application.usecase.UpdateAnimalUseCase;
import com.animal.demo.domain.repository.AnimalRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfiguration {
    @Bean public CreateAnimalUseCase createAnimalUseCase(AnimalRepository repository) { return new CreateAnimalUseCase(repository); }
    @Bean public GetAnimalByIdUseCase getAnimalByIdUseCase(AnimalRepository repository) { return new GetAnimalByIdUseCase(repository); }
    @Bean public ListAnimalsUseCase listAnimalsUseCase(AnimalRepository repository) { return new ListAnimalsUseCase(repository); }
    @Bean public UpdateAnimalUseCase updateAnimalUseCase(AnimalRepository repository) { return new UpdateAnimalUseCase(repository); }
    @Bean public DeleteAnimalUseCase deleteAnimalUseCase(AnimalRepository repository) { return new DeleteAnimalUseCase(repository); }
}
