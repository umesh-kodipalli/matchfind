# person-backend

A Spring Boot REST API that receives two person names from a React frontend
and stores them in MySQL.

## Tech stack

Java 21 · Spring Boot 3.5.x · Spring Web · Spring Data JPA · MySQL · Maven ·
Hibernate · Lombok · Jakarta Validation

## Project structure

```
person-backend/
├── pom.xml
├── sql/
│      schema.sql                     # reference schema (Hibernate creates this automatically)
└── src/
    ├── main/
    │   ├── java/com/example/personbackend/
    │   │   ├── PersonBackendApplication.java
    │   │   ├── controller/PersonController.java
    │   │   ├── service/PersonService.java
    │   │   ├── repository/PersonRepository.java
    │   │   ├── entity/Person.java
    │   │   ├── dto/
    │   │   │      PersonRequest.java
    │   │   │      PersonResponse.java
    │   │   └── exception/
    │   │          GlobalExceptionHandler.java
    │   │          ResourceNotFoundException.java
    │   │          ErrorResponse.java          # consistent error body shape (small addition, see note below)
    │   └── resources/application.properties
    └── test/java/com/example/personbackend/
           PersonBackendApplicationTests.java
           controller/PersonControllerTest.java
```

> **Note on structure:** everything matches the requested layout, with one
> small addition — `exception/ErrorResponse.java`. The brief's `dto` package
> asks for exactly `PersonRequest` and `PersonResponse`, so rather than add a
> third DTO for the POST success body (`{"message": ..., "id": ...}`), that
> tiny two-field acknowledgement is built directly in the controller as a
> `Map`. `ErrorResponse` gives every error from `GlobalExceptionHandler` one
> consistent JSON shape instead of ad-hoc maps in five different handlers.

## Prerequisites

- JDK 21
- Maven 3.8+
- A running MySQL server (the app will create the `persondb` schema on
  first connection if your MySQL user has `CREATE` privileges; the table
  itself is created by Hibernate)

## Setup

1. Open `src/main/resources/application.properties` and set your real MySQL
   credentials:

   ```properties
   spring.datasource.username=root
   spring.datasource.password=YOUR_PASSWORD
   ```

2. Build:

   ```bash
   mvn clean install
   ```

## Run

```bash
mvn spring-boot:run
```

or, after `mvn clean package`:

```bash
java -jar target/person-backend-0.0.1-SNAPSHOT.jar
```

The API starts on **http://localhost:5000**.

## API reference

### Create — `POST /api/names`

Request:
```json
{ "person1": "Alice", "person2": "Bob" }
```

Success (`201 Created`):
```json
{ "message": "Names stored successfully", "id": 1 }
```

Validation error (`400 Bad Request`) — e.g. `person2` blank:
```json
{
  "timestamp": "2026-06-28T10:30:00",
  "status": 400,
  "error": "Validation Failed",
  "message": "One or more fields are invalid",
  "fieldErrors": { "person2": "person2 must not be blank" }
}
```

### List all — `GET /api/names`

`200 OK`:
```json
[
  { "id": 1, "person1": "Alice", "person2": "Bob", "createdAt": "2026-06-28T10:30:00" }
]
```

### Get one — `GET /api/names/{id}`

`200 OK` on a match, or `404 Not Found`:
```json
{
  "timestamp": "2026-06-28T10:31:00",
  "status": 404,
  "error": "Not Found",
  "message": "Person record not found with id: 99"
}
```

### Delete — `DELETE /api/names/{id}`

`204 No Content` on success, or the same `404` shape as above if the id
doesn't exist.

## Postman quick reference

| Method | URL | Body |
|---|---|---|
| POST | `http://localhost:5000/api/names` | raw JSON: `{"person1":"Alice","person2":"Bob"}` |
| GET | `http://localhost:5000/api/names` | — |
| GET | `http://localhost:5000/api/names/1` | — |
| DELETE | `http://localhost:5000/api/names/1` | — |

For POST, in Postman: Body → raw → JSON, then paste the request body above.

Equivalent curl commands:
```bash
curl -X POST http://localhost:5000/api/names \
  -H "Content-Type: application/json" \
  -d '{"person1":"Alice","person2":"Bob"}'

curl http://localhost:5000/api/names
curl http://localhost:5000/api/names/1
curl -X DELETE http://localhost:5000/api/names/1
```

## How each layer works

- **`entity/Person.java`** — the JPA model mapped to the `person_names`
  table. `@PrePersist` stamps `createdAt` automatically, so no caller ever
  has to set it.
- **`repository/PersonRepository.java`** — a `JpaRepository<Person, Long>`.
  Spring Data generates `save`, `findAll`, `findById`, and `delete` from the
  interface alone; no implementation code needed.
- **`service/PersonService.java`** — holds all business logic: trimming
  input, mapping entities to response DTOs, and throwing
  `ResourceNotFoundException` when a lookup misses. The controller depends
  on this service, never on the repository directly.
- **`controller/PersonController.java`** — thin HTTP layer. Validates
  incoming JSON with `@Valid`, calls the service, and shapes the
  `ResponseEntity` (status code + body) for each endpoint.
- **`dto/PersonRequest.java` / `PersonResponse.java`** — the API's public
  contract, kept separate from the `Person` entity so the database schema
  and the JSON shape can change independently of each other.
- **`exception/GlobalExceptionHandler.java`** — a single `@ControllerAdvice`
  that turns every failure (validation, not-found, bad id format, malformed
  JSON, or anything unexpected) into the same `ErrorResponse` JSON shape and
  the correct HTTP status, so no controller method needs its own try/catch.

## How the React frontend connects

The controller is annotated `@CrossOrigin(origins = "http://localhost:5173")`
— the default Vite dev server port — so a browser-based React app running
there can call this API directly without a CORS error. From React, that's
just:

```js
axios.post("http://localhost:5000/api/names", { person1, person2 });
```

If your frontend runs on a different port or domain, update the
`@CrossOrigin` value in `PersonController.java` to match (or list multiple
origins: `@CrossOrigin(origins = {"http://localhost:5173", "https://yourapp.com"})`).

## A note on verification

This project was hand-written and reviewed line-by-line, but it could not be
compiled in the sandbox this was generated in — that environment only has
network access to a small allowlist of domains (npm, PyPI, GitHub, etc.) and
not to Maven Central, so `mvn compile` fails on the very first dependency
download with a 403, before any of this project's own code is even touched.
Running `mvn clean install` in a normal environment with full internet
access should build it without modification; let me know if you hit
anything and I'll fix it.
