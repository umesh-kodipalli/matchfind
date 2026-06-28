package com.example.personbackend;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class PersonBackendApplicationTests {

    @Test
    void contextLoads() {
        // Verifies the Spring context starts cleanly with all beans wired.
        // Note: this needs a reachable MySQL instance matching
        // application.properties, since no in-memory test profile is
        // configured. See README for a quick way to point this at a
        // disposable database if you want to run it in CI.
    }
}
