package com.ninyo.pet.mapper;

import com.ninyo.pet.dto.PetDto;
import com.ninyo.pet.model.Pet;

public class PetMapper {

    public static PetDto toDto(Pet pet) {
        if (pet == null) return null;

        return PetDto.builder()
                .id(pet.getId())
                .version(pet.getVersion())
                .name(pet.getName())
                .species(pet.getSpecies())
                .age(pet.getAge())
                .ownerName(pet.getOwnerName())
                .build();
    }

    public static Pet toEntity(PetDto dto) {
        if (dto == null) return null;

        return Pet.builder()
                .id(dto.getId())
                .version(dto.getVersion())
                .name(dto.getName())
                .species(dto.getSpecies())
                .age(dto.getAge())
                .ownerName(dto.getOwnerName())
                .build();
    }
}