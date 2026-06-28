package com.example.personbackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Entry point for the person-backend service.
 *
 * Run with: mvn spring-boot:run
 * or:       java -jar target/person-backend-0.0.1-SNAPSHOT.jar
 */
@SpringBootApplication
public class PersonBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(PersonBackendApplication.class, args);
    }
}
