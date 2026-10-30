package com.ninyo.pet.controller;

import com.ninyo.pet.dto.PetDto;
import com.ninyo.pet.dto.PetSearchRequest;
import com.ninyo.pet.service.PetService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pets")
@RequiredArgsConstructor
public class PetController {

    private final PetService petService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Long create(@Valid @RequestBody PetDto petDto) {
        return petService.create(petDto);
    }

    @GetMapping("/{id}")
    public PetDto findById(@PathVariable Long id) {
        return petService.findById(id);
    }

    @GetMapping
    public List<PetDto> findAllOrSearch(PetSearchRequest request) {
        if (request.getName() == null &&
                request.getAge() == null &&
                request.getOwnerName() == null) {

            return petService.findAll();
        }

        return petService.search(request);
    }

    @PutMapping("/{id}")
    public PetDto update(@PathVariable Long id,
                         @Valid @RequestBody PetDto petDto) {
        return petService.update(id, petDto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        petService.delete(id);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAll() {
        petService.deleteAll();
    }
}