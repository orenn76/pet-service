package com.ninyo.pet.controllers;

import com.ninyo.pet.model.Pet;
import com.ninyo.pet.model.Species;
import com.ninyo.pet.repository.PetRepository;
import com.ninyo.pet.repository.SpringDataPetRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.util.Map;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class PetControllerIntegrationTest {

    private Long existingId;

    @Value("${server.servlet.context-path:/api}")
    private String contextPath;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PetRepository petRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private static final String BASE = "/pets";

    @BeforeEach
    void setUp() {
        petRepository.deleteAll();

        Pet p1 = Pet.builder()
                .name("Buddy")
                .species(Species.DOG)
                .age(3)
                .ownerName("Alice")
                .build();

        Pet p2 = Pet.builder()
                .name("Mittens")
                .species(Species.CAT)
                .age(2)
                .ownerName("Bob")
                .build();

        existingId = petRepository.save(p1).getId();
        petRepository.save(p2);
    }

    @Test
    void testCreate() throws Exception {
        Map<String, Object> dto = Map.of(
                "name", "Rex",
                "species", "Dog",
                "age", 4,
                "ownerName", "Carol"
        );

        mockMvc.perform(post(contextPath + BASE)
                        .contextPath(contextPath)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated());

        mockMvc.perform(get(contextPath + BASE)
                        .contextPath(contextPath))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)));
    }

    @Test
    void testFindById() throws Exception {
        mockMvc.perform(get(contextPath + BASE + "/" + existingId)
                        .contextPath(contextPath))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(existingId))
                .andExpect(jsonPath("$.name").value("Buddy"))
                .andExpect(jsonPath("$.version").value(0));
    }

    @Test
    void testFindAll() throws Exception {
        mockMvc.perform(get(contextPath + BASE)
                        .contextPath(contextPath))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    void testSearchByName() throws Exception {
        mockMvc.perform(get(contextPath + BASE)
                        .contextPath(contextPath)
                        .param("name", "Buddy"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name").value("Buddy"));
    }

    @Test
    void testUpdate() throws Exception {
        Map<String, Object> updateDto = Map.of(
                "name", "BuddyUpdated",
                "species", "Dog",
                "age", 5,
                "ownerName", "AliceUpdated",
                "version", 0
        );

        mockMvc.perform(put(contextPath + BASE + "/" + existingId)
                        .contextPath(contextPath)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("BuddyUpdated")))
                .andExpect(jsonPath("$.age", is(5)))
                .andExpect(jsonPath("$.version", is(1)));
    }

    @Test
    void testUpdateOptimisticLockConflict() throws Exception {
        Map<String, Object> firstUpdate = Map.of(
                "name", "BuddyUpdated",
                "species", "Dog",
                "age", 5,
                "ownerName", "Alice",
                "version", 0
        );

        mockMvc.perform(put(contextPath + BASE + "/" + existingId)
                        .contextPath(contextPath)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(firstUpdate)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.version", is(1)));

        Map<String, Object> staleUpdate = Map.of(
                "name", "StaleUpdate",
                "species", "Dog",
                "age", 6,
                "ownerName", "Alice",
                "version", 0
        );

        mockMvc.perform(put(contextPath + BASE + "/" + existingId)
                        .contextPath(contextPath)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(staleUpdate)))
                .andExpect(status().isConflict());
    }

    @Test
    void testDelete() throws Exception {
        mockMvc.perform(delete(contextPath + BASE + "/" + existingId)
                        .contextPath(contextPath))
                .andExpect(status().isNoContent());

        mockMvc.perform(get(contextPath + BASE + "/" + existingId)
                        .contextPath(contextPath))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDeleteAll() throws Exception {

        mockMvc.perform(delete(contextPath + BASE)
                        .contextPath(contextPath))
                .andExpect(status().isNoContent());

        mockMvc.perform(get(contextPath + BASE)
                        .contextPath(contextPath))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    void testFindByNonExistId() throws Exception {
        mockMvc.perform(get(contextPath + BASE + "/999999")
                        .contextPath(contextPath))
                .andExpect(status().isNotFound());
    }
}