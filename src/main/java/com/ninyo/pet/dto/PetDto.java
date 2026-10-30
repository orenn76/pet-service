package com.ninyo.pet.dto;

import com.ninyo.pet.model.Species;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PetDto {

    private Long id;

    private Long version;

    @NotBlank(message = "Name is required")
    private String name;

    @NotNull(message = "Species is required")
    private Species species;

    @Min(value = 0, message = "Age must be greater than or equal to 0")
    private Integer age;

    private String ownerName;
}