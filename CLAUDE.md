# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project

Spring Boot 4.1 (Java 17, Gradle) REST API exposing CRUD for a single `Person` resource at `/persons`, backed by JPA and PostgreSQL. Lombok is used throughout.

## Commands

Use `./gradlew` (Git Bash) or `gradlew.bat` (PowerShell/cmd).

- Build: `./gradlew build`
- Run the app: `./gradlew bootRun` (port 8080; needs a reachable Postgres, see Database)
- Run all tests: `./gradlew test`
- Run a single test: `./gradlew test --tests "com.example.demo.service.PersonServiceTest.update"`

## Tests

`./gradlew test` needs no Postgres. Keep it that way: Flyway and Hibernate validation run on startup, so any `@SpringBootTest` must point the datasource at in-memory H2 (`testRuntimeOnly 'com.h2database:h2'`) in PostgreSQL mode, as `PersonControllerIT` does, so migrations must stay H2-compatible.

- `PersonControllerIT`: integration test with the full context and `@AutoConfigureMockMvc`. It overrides the datasource with `@SpringBootTest(properties = ...)`, seeds rows with its `save(...)` helper, sends JSON text blocks, asserts the response with `jsonPath`, and checks persisted rows through `PersonRepository` (cleaned up in `@AfterEach`). Not-found paths use `NONEXISTENT_ID` (`Long.MAX_VALUE`). The `delete` test method shadows the static `delete(...)` request builder, so it is called as `MockMvcRequestBuilders.delete`. This is where service-layer validation is exercised.
- `PersonControllerTest` / `PersonServiceTest`: `@ExtendWith(MockitoExtension.class)` with `@InjectMocks` on the class under test and `@Mock` for its dependency. Each test builds expected `PersonDto`s field by field, stubs with `when(...)`, and asserts with `assertEquals` (Lombok `@Data` equality). Not-found paths use `assertThrows(NoSuchElementException.class, ...)`.
- `GlobalExceptionHandlerTest`: instantiates the handler directly, builds a real exception per handler, and asserts both the status and the full `ErrorDto`.
- Test methods are named after the method under test (`readById`/`readPage` for the `read` overloads, `...NotFound` for error paths).
- Validation (`@Valid` on service methods) is not exercised by the unit tests, since it is applied by a Spring proxy; cover it in the integration test.

## Architecture

Layered: `controller` → `service` → `repository` → `entity`, with `dto` as the API contract and `exception` for error mapping.

- **DTO/entity split**: controllers and services speak `PersonDto` only; `PersonService` converts to/from the `Person` entity with `BeanUtils.copyProperties`, so DTO and entity field names must stay aligned. JSON uses snake_case via `@JsonProperty` on the DTO (`first_name`, `last_name`, `date_of_birth`); `id` is read-only in JSON.
- **Validation happens in the service layer, not the controller**: `PersonService` is `@Validated` and its methods take `@Valid PersonDto`, so invalid input raises `ConstraintViolationException` (not `MethodArgumentNotValidException`). Length limits come from `Person.MAX_NAME_LENGTH`, shared by the entity column definitions and DTO constraints.
- **Not-found handling**: services throw `NoSuchElementException` (e.g. `Optional.orElseThrow()`), which `GlobalExceptionHandler` maps to 404. Exception: `delete` calls `deleteById`, which silently ignores missing ids, so `DELETE` of a nonexistent person returns 204.
- **Error responses**: all errors go through `GlobalExceptionHandler` (`@RestControllerAdvice`) and return `ErrorDto` (`type` = exception class name, `description`, `detail` = exception message). Besides the domain mappings described here, it maps framework errors: unknown endpoint (`NoResourceFoundException`) → 404, unsupported HTTP method (`HttpRequestMethodNotSupportedException`) → 405, unparseable path variable such as `/persons/foo` (`MethodArgumentTypeMismatchException`) → 400, and malformed JSON body (`HttpMessageNotReadableException`) → 400. A catch-all `Exception` handler returns 500, so an exception without a dedicated handler surfaces as 500. When adding new failure modes, add a handler there rather than returning ad-hoc responses.
- **Concurrent modification**: `update` checks `existsById` before saving; if the person is deleted by another request in between, Hibernate throws `ObjectOptimisticLockingFailureException`, mapped to 409. `Person` has no `@Version` field, so concurrent updates of the same person are not detected (last write wins).
- **Listing** `GET /persons` accepts Spring Data `Pageable` params (`page`, `size`, `sort`); an invalid sort property yields `PropertyReferenceException` → 400. `DemoApplication` enables `@EnableSpringDataWebSupport(pageSerializationMode = VIA_DTO)`, so pages serialize as `{"content": [...], "page": {"size", "number", "totalElements", "totalPages"}}`.

## Database

Configured in `src/main/resources/application.yml`:
- PostgreSQL connection is read from `DB_URL`, `DB_USERNAME`, `DB_PASSWORD`, defaulting to a local database (`jdbc:postgresql://localhost:5432/demo`, user `sa`).
- Schema is managed by Flyway migrations in `src/main/resources/db/migration` (`V<n>__<description>.sql`), applied on startup. Hibernate runs with `ddl-auto: validate`, so any entity change (including `Person.MAX_NAME_LENGTH`) needs a matching new migration; never edit an already-applied one.
- `show-sql: true` logs all SQL.
