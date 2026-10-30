package com.ninyo.pet.services;

import com.ninyo.pet.dto.PetDto;
import com.ninyo.pet.dto.PetSearchRequest;
import com.ninyo.pet.exception.PetNotFoundException;
import com.ninyo.pet.mapper.PetMapper;
import com.ninyo.pet.model.Pet;
import com.ninyo.pet.model.Species;
import com.ninyo.pet.repository.PetRepository;
import com.ninyo.pet.service.PetService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PetServiceTest {

    private static final Long PET_ID = 1L;
    private static final String PET_NAME = "Buddy";

    @Mock
    private PetRepository petRepository;

    @InjectMocks
    private PetService petService;

    @Test
    public void testCreate() {
        Pet toSave = Pet.builder()
                .name(PET_NAME)
                .species(Species.DOG)
                .age(3)
                .ownerName("Alice")
                .build();
        Pet saved = toSave.toBuilder().id(PET_ID).build();
        when(petRepository.save(any(Pet.class))).thenReturn(saved);

        Long resultId = petService.create(PetMapper.toDto(toSave));

        verify(petRepository).save(any(Pet.class));
        Assertions.assertEquals(PET_ID, resultId);
    }

    @Test
    public void testFindById() {
        Pet pet = Pet.builder()
                .id(PET_ID)
                .name(PET_NAME)
                .species(Species.DOG)
                .age(3)
                .ownerName("Alice")
                .build();
        when(petRepository.findById(PET_ID)).thenReturn(Optional.of(pet));

        PetDto result = petService.findById(PET_ID);

        verify(petRepository).findById(PET_ID);
        Assertions.assertEquals(PetMapper.toDto(pet), result);
    }

    @Test
    public void testFindAll() {
        Pet p1 = Pet.builder().id(1L).name("A").species(Species.CAT).age(2).ownerName("X").build();
        Pet p2 = Pet.builder().id(2L).name("B").species(Species.DOG).age(4).ownerName("Y").build();
        List<Pet> pets = List.of(p1, p2);
        when(petRepository.findAll()).thenReturn(pets);

        List<PetDto> result = petService.findAll();

        verify(petRepository).findAll();
        Assertions.assertEquals(List.of(PetMapper.toDto(p1), PetMapper.toDto(p2)), result);
    }

    @Test
    public void testSearchByName() {
        Pet pet = Pet.builder()
                .id(PET_ID)
                .name(PET_NAME)
                .species(Species.DOG)
                .age(3)
                .ownerName("Alice")
                .build();
        when(petRepository.search(PET_NAME, null, null))
                .thenReturn(List.of(pet));
        PetSearchRequest request = new PetSearchRequest();
        request.setName(PET_NAME);

        List<PetDto> result = petService.search(request);

        verify(petRepository).search(PET_NAME, null, null);
        Assertions.assertEquals(List.of(PetMapper.toDto(pet)), result);
    }

    @Test
    public void testUpdate() {
        Pet existing = Pet.builder()
                .id(PET_ID)
                .version(0L)
                .name("Old")
                .species(Species.CAT)
                .age(5)
                .ownerName("OldOwner")
                .build();
        PetDto updateDto = PetDto.builder()
                .version(0L)
                .name("New")
                .species(Species.CAT)
                .age(6)
                .ownerName("NewOwner")
                .build();
        Pet updated = existing.toBuilder()
                .version(1L)
                .name(updateDto.getName())
                .species(updateDto.getSpecies())
                .age(updateDto.getAge())
                .ownerName(updateDto.getOwnerName())
                .build();
        when(petRepository.findById(PET_ID)).thenReturn(Optional.of(existing));
        when(petRepository.save(any(Pet.class))).thenReturn(updated);

        PetDto result = petService.update(PET_ID, updateDto);

        verify(petRepository).findById(PET_ID);
        verify(petRepository).save(any(Pet.class));
        Assertions.assertEquals(PetMapper.toDto(updated), result);
    }

    @Test
    public void testDelete() {
        when(petRepository.existsById(PET_ID)).thenReturn(true);
        doNothing().when(petRepository).deleteById(PET_ID);

        petService.delete(PET_ID);

        verify(petRepository).existsById(PET_ID);
        verify(petRepository).deleteById(PET_ID);
    }

    @Test
    public void testDeleteAll() {
        doNothing().when(petRepository).deleteAll();

        petService.deleteAll();

        verify(petRepository).deleteAll();
    }

    @Test
    public void testDeleteNotFound() {
        when(petRepository.existsById(PET_ID)).thenReturn(false);

        Assertions.assertThrows(PetNotFoundException.class, () -> petService.delete(PET_ID));

        verify(petRepository).existsById(PET_ID);
        verify(petRepository, never()).deleteById(anyLong());
    }

}
