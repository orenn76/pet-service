package com.ninyo.pet.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
        name = "pets",
        indexes = {
                @Index(name = "idx_pet_name", columnList = "name")
        }
)
@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED) // For Hibernate
@AllArgsConstructor // For application code
public class Pet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    private Long version;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Species species;

    private Integer age;

    @Column(length = 100)
    private String ownerName;
}