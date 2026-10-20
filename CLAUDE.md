# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project

Spring Boot 4.1 (Java 17, Gradle) REST API exposing CRUD for a single `Person` resource at `/persons`, backed by JPA and a file-based H2 database. Lombok is used throughout.

## Commands

Use `./gradlew` (Git Bash) or `gradlew.bat` (PowerShell/cmd).

- Build: `./gradlew build`
- Run the app: `./gradlew bootRun` (port 8080)
- Run all tests: `./gradlew test`
- Run a single test: `./gradlew test --tests "com.example.demo.SomeTest.someMethod"`

There is currently no `src/test` directory; test dependencies (`spring-boot-starter-webmvc-test`, `spring-boot-starter-data-jpa-test`) are already declared.

## Architecture

Layered: `controller` → `service` → `repository` → `entity`, with `dto` as the API contract and `exception` for error mapping.

- **DTO/entity split**: controllers and services speak `PersonDto` only; `PersonService` converts to/from the `Person` entity with `BeanUtils.copyProperties`, so DTO and entity field names must stay aligned. JSON uses snake_case via `@JsonProperty` on the DTO (`first_name`, `last_name`, `date_of_birth`); `id` is read-only in JSON.
- **Validation happens in the service layer, not the controller**: `PersonService` is `@Validated` and its methods take `@Valid PersonDto`, so invalid input raises `ConstraintViolationException` (not `MethodArgumentNotValidException`). Length limits come from `Person.MAX_NAME_LENGTH`, shared by the entity column definitions and DTO constraints.
- **Not-found handling**: services throw `NoSuchElementException` (e.g. `Optional.orElseThrow()`), which `GlobalExceptionHandler` maps to 404.
- **Error responses**: all errors go through `GlobalExceptionHandler` (`@RestControllerAdvice`) and return `ErrorDto` (`type` = exception class name, `description`, `detail` = exception message). When adding new failure modes, add a handler there rather than returning ad-hoc responses.
- **Listing** `GET /persons` accepts Spring Data `Pageable` params (`page`, `size`, `sort`); an invalid sort property yields `PropertyReferenceException` → 400.

## Database

Configured in `src/main/resources/application.yml`:
- H2 file DB at `~/h2/persons` (persists across runs, outside the repo); schema managed by `ddl-auto: update`.
- H2 console at `http://localhost:8080/h2-console` (JDBC URL `jdbc:h2:file:~/h2/persons`, user `sa`).
- `show-sql: true` logs all SQL.
