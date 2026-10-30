package com.ninyo.pet.service;

import com.ninyo.pet.dto.PetDto;
import com.ninyo.pet.dto.PetSearchRequest;
import com.ninyo.pet.exception.PetNotFoundException;
import com.ninyo.pet.mapper.PetMapper;
import com.ninyo.pet.model.Pet;
import com.ninyo.pet.repository.PetRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class PetService {

    private final PetRepository petRepository;

    public Long create(PetDto dto) {
        Pet pet = PetMapper.toEntity(dto);
        Pet saved = petRepository.save(pet);
        log.info("Created pet with id {}", saved.getId());
        return saved.getId();
    }

    public PetDto findById(Long id) {
        Pet pet = petRepository.findById(id)
                .orElseThrow(() ->
                        new PetNotFoundException("Pet with id " + id + " could not be found"));
        return PetMapper.toDto(pet);
    }

    public List<PetDto> findAll() {
        return petRepository.findAll()
                .stream()
                .map(PetMapper::toDto)
                .toList();
    }

    public List<PetDto> search(PetSearchRequest request) {
        String name = request.getName();
        Integer age = request.getAge();
        String ownerName = request.getOwnerName();

        List<Pet> pets = petRepository.search(name, age, ownerName);

        return pets.stream()
                .map(PetMapper::toDto)
                .toList();
    }

    public PetDto update(Long id, PetDto dto) {
        Pet existing = petRepository.findById(id)
                .orElseThrow(() ->
                        new PetNotFoundException("Pet with id " + id + " could not be found"));

        Pet updated = existing.toBuilder()
                .name(dto.getName())
                .species(dto.getSpecies())
                .age(dto.getAge())
                .ownerName(dto.getOwnerName())
                .version(dto.getVersion() != null ? dto.getVersion() : existing.getVersion())
                .build();

        Pet saved = petRepository.save(updated);
        log.info("Updated pet with id {}", id);
        return PetMapper.toDto(saved);
    }

    public void delete(Long id) {
        if (!petRepository.existsById(id)) {
            throw new PetNotFoundException("Pet with id " + id + " could not be found");
        }
        petRepository.deleteById(id);
        log.info("Deleted pet with id {}", id);
    }

    public void deleteAll() {
        petRepository.deleteAll();
    }
}