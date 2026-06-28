package com.example.personbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Shape of a stored record as returned by GET /api/names and
 * GET /api/names/{id}:
 * { "id": 1, "person1": "Alice", "person2": "Bob", "createdAt": "..." }
 *
 * The POST /api/names success body ({"message": ..., "id": ...}) is
 * deliberately not modeled here — it's a different, smaller shape, and is
 * built directly in the controller rather than adding a third DTO class
 * for a two-field acknowledgement.
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PersonResponse {
    private Long id;
    private String person1;
    private String person2;
    private Integer percentage;
    private String message;
    private LocalDateTime createdAt;
}
