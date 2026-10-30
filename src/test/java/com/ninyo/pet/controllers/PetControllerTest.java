package com.ninyo.pet.controllers;

import com.ninyo.pet.controller.PetController;
import com.ninyo.pet.dto.PetDto;
import com.ninyo.pet.dto.PetSearchRequest;
import com.ninyo.pet.mapper.PetMapper;
import com.ninyo.pet.model.Pet;
import com.ninyo.pet.model.Species;
import com.ninyo.pet.service.PetService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PetController.class)
class PetControllerTest {

    private static final Long PET_ID = 1L;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private PetService petService;

    @Test
    void testCreate() throws Exception {
        Long createdId = 1L;
        PetDto request = PetDto.builder()
                .name("Buddy")
                .species(Species.DOG)
                .age(3)
                .ownerName("Alice")
                .build();
        when(petService.create(any())).thenReturn(createdId);

        mockMvc.perform(post("/pets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonStr(request)))
                .andExpect(status().isCreated())
                .andExpect(content().string(createdId.toString()));

        verify(petService).create(any());
    }

    @Test
    void testFindById() throws Exception {
        Pet pet = Pet.builder()
                .id(PET_ID)
                .name("Buddy")
                .species(Species.DOG)
                .age(3)
                .ownerName("Alice")
                .build();
        PetDto dto = PetMapper.toDto(pet);
        when(petService.findById(PET_ID)).thenReturn(dto);

        mockMvc.perform(get("/pets/{id}", PET_ID))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(jsonStr(dto)));

        verify(petService).findById(PET_ID);
    }

    @Test
    void testFindAll() throws Exception {
        Pet p1 = Pet.builder().id(1L).name("A").species(Species.CAT).age(2).ownerName("X").build();
        Pet p2 = Pet.builder().id(2L).name("B").species(Species.DOG).age(4).ownerName("Y").build();
        List<PetDto> dtos = List.of(
                PetMapper.toDto(p1),
                PetMapper.toDto(p2)
        );
        when(petService.findAll()).thenReturn(dtos);

        mockMvc.perform(get("/pets"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(jsonStr(dtos)));

        verify(petService).findAll();
        verify(petService, never()).search(any());
    }

    @Test
    void testSearchByName() throws Exception {
        String name = "Buddy";
        Pet pet = Pet.builder()
                .id(1L)
                .name(name)
                .species(Species.DOG)
                .age(3)
                .ownerName("Alice")
                .build();
        PetDto dto = PetMapper.toDto(pet);
        when(petService.search(any())).thenReturn(List.of(dto));

        mockMvc.perform(get("/pets")
                        .param("name", name))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(jsonStr(List.of(dto))));

        ArgumentCaptor<PetSearchRequest> captor =
                ArgumentCaptor.forClass(PetSearchRequest.class);
        verify(petService).search(captor.capture());
        assertEquals(name, captor.getValue().getName());
        verify(petService, never()).findAll();
    }

    @Test
    void testUpdate() throws Exception {
        Long id = 1L;
        PetDto request = PetDto.builder()
                .name("Updated")
                .species(Species.DOG)
                .age(5)
                .ownerName("Bob")
                .build();
        PetDto response = PetDto.builder()
                .id(id)
                .name("Updated")
                .species(Species.DOG)
                .age(5)
                .ownerName("Bob")
                .build();
        when(petService.update(eq(id), any())).thenReturn(response);

        mockMvc.perform(put("/pets/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonStr(request)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(jsonStr(response)));

        verify(petService).update(eq(id), any());
    }

    @Test
    void testDelete() throws Exception {
        long id = 1L;

        mockMvc.perform(delete("/pets/{id}", id))
                .andExpect(status().isNoContent());

        verify(petService).delete(id);
    }

    private String jsonStr(Object obj) throws Exception {
        return objectMapper.writeValueAsString(obj);
    }

    @Test
    void testDeleteAll() throws Exception {

        mockMvc.perform(delete("/pets"))
                .andExpect(status().isNoContent());

        verify(petService).deleteAll();
    }

}