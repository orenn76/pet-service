package com.ninyo.pet.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Arrays;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

public enum Species {

    DOG("Dog"),
    CAT("Cat"),
    RABBIT("Rabbit");

    private static final Map<String, Species> LOOKUP =
            Arrays.stream(values())
                    .collect(Collectors.toMap(
                            s -> s.value.toLowerCase(Locale.ROOT),
                            s -> s
                    ));

    private final String value;

    Species(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static Species from(String value) {
        if (value == null) return null;

        Species species = LOOKUP.get(value.toLowerCase(Locale.ROOT));
        if (species == null) {
            throw new IllegalArgumentException("Invalid species: " + value);
        }

        return species;
    }

}