package com.example.personbackend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Shape of the incoming JSON body for creating a person-names record:
 * { "person1": "Alice", "person2": "Bob" }
 *
 * This is intentionally separate from the Person entity so the API
 * contract and the persistence model can evolve independently.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PersonRequest {

    @NotBlank(message = "person1 must not be blank")
    @Size(max = 255, message = "person1 must be at most 255 characters")
    private String person1;

    @NotBlank(message = "person2 must not be blank")
    @Size(max = 255, message = "person2 must be at most 255 characters")
    private String person2;
}
