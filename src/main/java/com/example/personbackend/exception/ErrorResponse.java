package com.example.personbackend.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Consistent error body returned by every handler in GlobalExceptionHandler,
 * e.g.:
 * {
 *   "timestamp": "2026-06-28T10:30:00",
 *   "status": 400,
 *   "error": "Validation Failed",
 *   "message": "One or more fields are invalid",
 *   "fieldErrors": { "person1": "person1 must not be blank" }
 * }
 */
@Getter
@Builder
@AllArgsConstructor
public class ErrorResponse {
    private LocalDateTime timestamp;
    private int status;
    private String error;
    private String message;

    // Only present for validation failures; omitted from the JSON entirely
    // otherwise so 404/500 responses don't show a stray "fieldErrors": null.
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Map<String, String> fieldErrors;
}
