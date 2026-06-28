package com.example.personbackend.controller;

import com.example.personbackend.dto.PersonRequest;
import com.example.personbackend.dto.PersonResponse;
import com.example.personbackend.service.PersonService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST Controller for handling Love Match requests.
 */
@RestController
@RequestMapping("/api/names")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class PersonController {

    private final PersonService personService;

    /**
     * POST /api/names
     * Stores the names, calculates the love percentage,
     * and returns the complete response.
     */
    @PostMapping
    public ResponseEntity<PersonResponse> createPerson(
            @Valid @RequestBody PersonRequest request) {

        PersonResponse response = personService.createPerson(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    /**
     * GET /api/names
     * Returns all stored love matches.
     */

    /**
     * GET /api/names/{id}
     * Returns one love match by ID.
     */

    /**
     * DELETE /api/names/{id}
     * Deletes a stored love match.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePerson(
            @PathVariable Long id) {

        personService.deletePerson(id);

        return ResponseEntity.noContent().build();
    }
}