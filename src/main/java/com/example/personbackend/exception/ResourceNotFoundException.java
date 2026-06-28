package com.example.personbackend.exception;

/**
 * Thrown when a requested person_names record doesn't exist.
 * Translated to HTTP 404 by {@link GlobalExceptionHandler}.
 */
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }
}
