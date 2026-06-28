package com.example.personbackend.service;

import com.example.personbackend.dto.PersonRequest;
import com.example.personbackend.dto.PersonResponse;
import com.example.personbackend.entity.Person;
import com.example.personbackend.exception.ResourceNotFoundException;
import com.example.personbackend.repository.PersonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PersonService {

    private final PersonRepository personRepository;

    /**
     * Creates a new record, calculates love percentage,
     * stores it in the database, and returns the response.
     */
    @Transactional
    public PersonResponse createPerson(PersonRequest request) {

        String person1 = request.getPerson1().trim();
        String person2 = request.getPerson2().trim();

        int percentage = calculateLovePercentage(person1, person2);

        Person person = Person.builder()
                .person1(person1)
                .person2(person2)
                .percentage(percentage)
                .build();

        Person savedPerson = personRepository.save(person);

        return PersonResponse.builder()
                .id(savedPerson.getId())
                .person1(savedPerson.getPerson1())
                .person2(savedPerson.getPerson2())
                .percentage(savedPerson.getPercentage())
                .message(getLoveMessage(percentage))
                .createdAt(savedPerson.getCreatedAt())
                .build();
    }

    /**
     * Returns all saved records.
     */
    @Transactional(readOnly = true)
    public List<PersonResponse> getAllPersons() {

        return personRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    /**
     * Returns one record by id.
     */
    @Transactional(readOnly = true)
    public PersonResponse getPersonById(Long id) {

        Person person = findOrThrow(id);

        return toResponse(person);
    }

    /**
     * Deletes one record.
     */
    @Transactional
    public void deletePerson(Long id) {

        Person person = findOrThrow(id);

        personRepository.delete(person);
    }

    /**
     * Finds a record or throws 404.
     */
    private Person findOrThrow(Long id) {

        return personRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Person record not found with id: " + id));
    }

    /**
     * Converts Entity -> DTO
     */
    private PersonResponse toResponse(Person person) {

        return PersonResponse.builder()
                .id(person.getId())
                .person1(person.getPerson1())
                .person2(person.getPerson2())
                .percentage(person.getPercentage())
                .message(getLoveMessage(person.getPercentage()))
                .createdAt(person.getCreatedAt())
                .build();
    }

    /**
     * Calculates a deterministic love percentage.
     * The same names will always return the same percentage.
     */
    private int calculateLovePercentage(String person1, String person2) {

        String combined = person1.toLowerCase().trim() +
                person2.toLowerCase().trim();

        return Math.abs(combined.hashCode()) % 101;
    }

    /**
     * Returns a romantic message based on the percentage.
     */
    private String getLoveMessage(int percentage) {

        if (percentage >= 95) {
            return "💍 Perfect Match ❤️";
        }

        if (percentage >= 80) {
            return "❤️ Soulmates";
        }

        if (percentage >= 60) {
            return "💕 Great Connection";
        }

        if (percentage >= 40) {
            return "😊 Good Friends";
        }

        return "😅 Better Luck Next Time";
    }
}