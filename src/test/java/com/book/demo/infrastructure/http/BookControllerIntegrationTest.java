package com.book.demo.infrastructure.http;

import com.book.demo.infrastructure.persistence.repository.SpringDataBookRepository;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.Year;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class BookControllerIntegrationTest {
    private static final String VALID_BOOK = """
            {"titulo":"Dom Casmurro","autor":"Machado de Assis","anoPublicacao":1899,
             "genero":"Romance","statusLeitura":"NAO_LIDO"}
            """;

    @Autowired private MockMvc mockMvc;
    @Autowired private SpringDataBookRepository repository;

    @BeforeEach
    void cleanDatabase() {
        repository.deleteAll();
    }

    @Test
    void shouldExecuteCompleteCrudFlow() throws Exception {
        MvcResult created = mockMvc.perform(post("/books").contentType(MediaType.APPLICATION_JSON)
                        .content(VALID_BOOK))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.titulo").value("Dom Casmurro"))
                .andExpect(jsonPath("$.autor").value("Machado de Assis"))
                .andExpect(jsonPath("$.genero").value("Romance"))
                .andExpect(jsonPath("$.anoPublicacao").value(1899))
                .andExpect(jsonPath("$.statusLeitura").value("NAO_LIDO"))
                .andReturn();
        String id = JsonPath.read(created.getResponse().getContentAsString(), "$.id");
        org.junit.jupiter.api.Assertions.assertEquals("/books/" + id,
                created.getResponse().getHeader("Location"));

        mockMvc.perform(get("/books"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(id));
        mockMvc.perform(get("/books/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusLeitura").value("NAO_LIDO"));
        mockMvc.perform(put("/books/{id}", id).contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"titulo":"1984","autor":"George Orwell","anoPublicacao":1949,
                                 "genero":"Distopia","statusLeitura":"LIDO"}
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.titulo").value("1984"))
                .andExpect(jsonPath("$.autor").value("George Orwell"))
                .andExpect(jsonPath("$.anoPublicacao").value(1949))
                .andExpect(jsonPath("$.genero").value("Distopia"))
                .andExpect(jsonPath("$.statusLeitura").value("LIDO"));
        mockMvc.perform(get("/books/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.titulo").value("1984"));
        mockMvc.perform(delete("/books/{id}", id)).andExpect(status().isNoContent());
        mockMvc.perform(get("/books/{id}", id))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));
        mockMvc.perform(get("/books"))
                .andExpect(status().isOk()).andExpect(content().json("[]"));
    }

    @Test
    void shouldReturnEmptyList() throws Exception {
        mockMvc.perform(get("/books")).andExpect(status().isOk()).andExpect(content().json("[]"));
    }

    @Test
    void shouldReturnFieldErrors() throws Exception {
        mockMvc.perform(post("/books").contentType(MediaType.APPLICATION_JSON).content("""
                        {"titulo":" ","autor":"","anoPublicacao":0,"genero":""}
                        """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.fields.titulo").exists())
                .andExpect(jsonPath("$.fields.autor").exists())
                .andExpect(jsonPath("$.fields.anoPublicacao").exists())
                .andExpect(jsonPath("$.fields.genero").exists())
                .andExpect(jsonPath("$.fields.statusLeitura").exists());
    }

    @ParameterizedTest
    @ValueSource(strings = {"{}", "{", "", "null", "[]"})
    void shouldRejectInvalidOrMissingBody(String body) throws Exception {
        mockMvc.perform(post("/books").contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isBadRequest());
    }

    @ParameterizedTest
    @ValueSource(strings = {"NAO_LIDO", "LENDO", "LIDO"})
    void shouldPersistEveryReadingStatus(String readingStatus) throws Exception {
        MvcResult created = mockMvc.perform(post("/books").contentType(MediaType.APPLICATION_JSON)
                        .content(VALID_BOOK.replace("NAO_LIDO", readingStatus)))
                .andExpect(status().isCreated()).andReturn();
        String id = JsonPath.read(created.getResponse().getContentAsString(), "$.id");
        mockMvc.perform(get("/books/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusLeitura").value(readingStatus));
    }

    @Test
    void shouldRejectInvalidEnumFutureYearAndLongText() throws Exception {
        for (String body : new String[] {
                VALID_BOOK.replace("NAO_LIDO", "DESCONHECIDO"),
                VALID_BOOK.replace("1899", String.valueOf(Year.now().getValue() + 1)),
                VALID_BOOK.replace("Dom Casmurro", "X".repeat(201)),
                VALID_BOOK.replace("Machado de Assis", "X".repeat(151)),
                VALID_BOOK.replace("Romance", "X".repeat(81))
        }) {
            mockMvc.perform(post("/books").contentType(MediaType.APPLICATION_JSON).content(body))
                    .andExpect(status().isBadRequest());
        }
        org.junit.jupiter.api.Assertions.assertEquals(0, repository.count());
    }

    @Test
    void shouldLeaveBookUnchangedAfterInvalidUpdate() throws Exception {
        MvcResult created = mockMvc.perform(post("/books").contentType(MediaType.APPLICATION_JSON)
                .content(VALID_BOOK)).andReturn();
        String id = JsonPath.read(created.getResponse().getContentAsString(), "$.id");
        mockMvc.perform(put("/books/{id}", id).contentType(MediaType.APPLICATION_JSON)
                        .content(VALID_BOOK.replace("1899", "-1")))
                .andExpect(status().isBadRequest());
        mockMvc.perform(get("/books/{id}", id))
                .andExpect(status().isOk()).andExpect(jsonPath("$.anoPublicacao").value(1899));
    }

    @Test
    void shouldReturnBadRequestForInvalidUuid() throws Exception {
        mockMvc.perform(get("/books/uuid-invalido")).andExpect(status().isBadRequest());
        mockMvc.perform(put("/books/uuid-invalido").contentType(MediaType.APPLICATION_JSON)
                .content(VALID_BOOK)).andExpect(status().isBadRequest());
        mockMvc.perform(delete("/books/uuid-invalido")).andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnNotFoundForUnknownBook() throws Exception {
        String id = UUID.randomUUID().toString();
        mockMvc.perform(get("/books/{id}", id)).andExpect(status().isNotFound());
        mockMvc.perform(put("/books/{id}", id).contentType(MediaType.APPLICATION_JSON)
                .content(VALID_BOOK)).andExpect(status().isNotFound());
        mockMvc.perform(delete("/books/{id}", id)).andExpect(status().isNotFound());
    }
}
