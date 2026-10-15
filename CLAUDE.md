# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project

Spring Boot 4.1 (Java 17 toolchain, Gradle 9 wrapper) REST API exposing CRUD for a single `Person` resource at `/persons`, backed by H2 via Spring Data JPA. Lombok is used throughout.

## Commands

Use `./gradlew` (bash) or `.\gradlew.bat` (PowerShell).

- Build: `./gradlew build`
- Run app: `./gradlew bootRun` (port 8080; H2 console at `/h2-console`)
- All tests: `./gradlew test`
- Single test class / method: `./gradlew test --tests com.example.demo.DemoApplicationTests` or `--tests 'com.example.demo.DemoApplicationTests.contextLoads'`

## Architecture

Layered: `controller` → `service` → `repository` → `entity`, with `dto` as the API contract and `exception.GlobalExceptionHandler` mapping exceptions to HTTP responses.

Non-obvious conventions that span files:

- **Validation happens in the service, not the controller.** `PersonService` is `@Validated` and its methods take `@Valid PersonDto`; the controller's `@RequestBody` has no `@Valid`. Failures therefore surface as `jakarta.validation.ConstraintViolationException` (handled → 400), not `MethodArgumentNotValidException`.
- **Not-found is signalled with `NoSuchElementException`** (e.g. `Optional.orElseThrow()`), which the global handler maps to 404. Every error response is an `ErrorDto` (`type` = exception class name, `description`, `detail`); add new exception mappings in `GlobalExceptionHandler`.
- **DTO ↔ entity mapping** uses `BeanUtils.copyProperties` in `PersonService`, so field names must match between `PersonDto` and `Person`. JSON uses snake_case via `@JsonProperty` on the DTO; `id` is read-only in JSON.
- **Column length constraints** are shared: `Person.MAX_NAME_LENGTH` drives both the JPA `@Column(length)` and the DTO `@Size`.
- **Pagination**: `GET /persons` accepts Spring `Pageable` params (`page`, `size`, `sort`). `DemoApplication` enables `PageSerializationMode.VIA_DTO`, so pages serialize as `{content, page:{...}}`. Sorting by an unknown property raises `PropertyReferenceException` → 400.

## Database

`application.yml` points to a **file-based** H2 DB at `~/h2/persons` with `ddl-auto: update` — data persists across runs, and there is no separate test profile, so `@SpringBootTest` tests use the same database file.

## Style

2-space indentation, Lombok (`@Data`, `@RequiredArgsConstructor`) with constructor injection via `final` fields.
