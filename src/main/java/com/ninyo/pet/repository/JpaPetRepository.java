package com.ninyo.pet.repository;

import com.ninyo.pet.model.Pet;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class JpaPetRepository implements PetRepository {

    private final SpringDataPetRepository repository;

    @Override
    public Pet save(Pet pet) {
        return repository.save(pet);
    }

    @Override
    public Optional<Pet> findById(Long id) {
        return repository.findById(id);
    }

    @Override
    public List<Pet> findAll() {
        return repository.findAll();
    }

    @Override
    public boolean existsById(Long id) {
        return repository.existsById(id);
    }

    @Override
    public List<Pet> search(String name, Integer age, String ownerName) {
        return repository.search(name, age, ownerName);
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    @Override
    public void deleteAll() {
        repository.deleteAll();
    }
}
