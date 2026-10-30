package com.ninyo.pet.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PetSearchRequest {

    private String name;
    private Integer age;
    private String ownerName;
    
}
