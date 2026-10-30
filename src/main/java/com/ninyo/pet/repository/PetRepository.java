package com.ninyo.pet.repository;

import com.ninyo.pet.model.Pet;

import java.util.List;
import java.util.Optional;

public interface PetRepository {

    Pet save(Pet pet);

    Optional<Pet> findById(Long id);

    List<Pet> findAll();

    boolean existsById(Long id);

    List<Pet> search(String name, Integer age, String ownerName);

    void deleteById(Long id);

    void deleteAll();
}
