package com.ninyo.pet.repository;

import com.ninyo.pet.model.Pet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SpringDataPetRepository extends JpaRepository<Pet, Long> {

    @Query("""
                SELECT p FROM Pet p
                WHERE (:name IS NULL OR LOWER(p.name) = LOWER(:name))
                  AND (:age IS NULL OR p.age = :age)
                  AND (:ownerName IS NULL OR LOWER(p.ownerName) = LOWER(:ownerName))
            """)
    List<Pet> search(String name, Integer age, String ownerName);
}


