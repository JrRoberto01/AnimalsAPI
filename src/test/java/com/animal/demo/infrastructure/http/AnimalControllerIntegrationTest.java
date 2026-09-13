package com.animal.demo.infrastructure.http;

import com.animal.demo.infrastructure.persistence.repository.SpringDataAnimalRepository;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class AnimalControllerIntegrationTest {
    @Autowired private MockMvc mockMvc;
    @Autowired private SpringDataAnimalRepository repository;

    @BeforeEach
    void cleanDatabase() {
        repository.deleteAll();
    }

    @Test
    void shouldExecuteCompleteCrudFlow() throws Exception {
        MvcResult created = mockMvc.perform(post("/animals").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nome\":\"Luna\",\"especie\":\"Gato\",\"idade\":2,\"sexo\":\"Femea\",\"disponivelAdocao\":true}"))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.nome").value("Luna"))
                .andReturn();
        String id = JsonPath.read(created.getResponse().getContentAsString(), "$.id");

        mockMvc.perform(get("/animals"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(id));
        mockMvc.perform(get("/animals/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.disponivelAdocao").value(true));
        mockMvc.perform(put("/animals/{id}", id).contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nome\":\"Luna\",\"especie\":\"Gato\",\"idade\":3,\"sexo\":\"Femea\",\"disponivelAdocao\":false}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idade").value(3))
                .andExpect(jsonPath("$.disponivelAdocao").value(false));
        mockMvc.perform(delete("/animals/{id}", id)).andExpect(status().isNoContent());
        mockMvc.perform(get("/animals/{id}", id))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));
    }

    @Test
    void shouldReturnBadRequestForInvalidBodyAndUuid() throws Exception {
        mockMvc.perform(post("/animals").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nome\":\"\",\"especie\":\"\",\"idade\":-1,\"sexo\":\"\",\"disponivelAdocao\":false}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.fields.nome").exists())
                .andExpect(jsonPath("$.fields.idade").exists());
        mockMvc.perform(get("/animals/uuid-invalido"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400));
    }

    @Test
    void shouldReturnNotFoundWhenUpdatingOrDeletingUnknownAnimal() throws Exception {
        String id = UUID.randomUUID().toString();
        mockMvc.perform(put("/animals/{id}", id).contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nome\":\"Luna\",\"especie\":\"Gato\",\"idade\":2,\"sexo\":\"Femea\",\"disponivelAdocao\":true}"))
                .andExpect(status().isNotFound());
        mockMvc.perform(delete("/animals/{id}", id))
                .andExpect(status().isNotFound());
    }
}
