package com.example.demo.service;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import com.example.demo.dto.PersonDto;
import com.example.demo.entity.Person;
import com.example.demo.repository.PersonRepository;
import jakarta.validation.ConstraintViolationException;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;

/**
 * Tests the {@code @Valid} constraints on {@link PersonService} method parameters.
 *
 * <p>Those constraints are enforced by a Spring AOP proxy around the service, not by the service
 * itself, so a plain {@code new PersonService(...)} would skip them. This test therefore starts a
 * small Spring context (not {@code @SpringBootTest}): no auto-configuration, no web layer, no JPA
 * and no H2 database are loaded. The context contains only the beans declared in {@link Config},
 * plus the mocked repository.
 */
@SpringJUnitConfig(PersonServiceValidationTest.Config.class)
class PersonServiceValidationTest {

  private static final String MAX_LENGTH_NAME = "a".repeat(Person.MAX_NAME_LENGTH);
  private static final String TOO_LONG_NAME = "a".repeat(Person.MAX_NAME_LENGTH + 1);

  /**
   * Everything the context loads for real.
   */
  @Configuration
  @Import(PersonService.class) // the real service under test
  static class Config {

    // Hibernate Validator, which evaluates @NotNull / @Size on PersonDto.
    @Bean
    static LocalValidatorFactoryBean validator() {
      return new LocalValidatorFactoryBean();
    }

    // Wraps @Validated beans (PersonService) in a proxy that validates @Valid arguments before
    // the method runs, throwing ConstraintViolationException on failure.
    @Bean
    static MethodValidationPostProcessor methodValidationPostProcessor() {
      return new MethodValidationPostProcessor();
    }

  }

  // The only mock: a Mockito mock registered in the context in place of the real Spring Data
  // repository, and injected into PersonService. It lets the tests check that invalid input
  // never reaches the repository, and lets the "accepts" tests stub save/existsById.
  @MockitoBean
  private PersonRepository personRepository;

  // The validating proxy around the real PersonService, not the bare service instance.
  @Autowired
  private PersonService personService;

  static Stream<Arguments> invalidPersons() {
    return Stream.of(
        Arguments.of("null first name", personDto(null, "Doe")),
        Arguments.of("null last name", personDto("John", null)),
        Arguments.of("first name too long", personDto(TOO_LONG_NAME, "Doe")),
        Arguments.of("last name too long", personDto("John", TOO_LONG_NAME)));
  }

  @ParameterizedTest(name = "{0}")
  @MethodSource("invalidPersons")
  void createRejectsInvalidPerson(String description, PersonDto personDto) {
    assertThatThrownBy(() -> personService.create(personDto))
        .isInstanceOf(ConstraintViolationException.class);
    verifyNoInteractions(personRepository);
  }

  @ParameterizedTest(name = "{0}")
  @MethodSource("invalidPersons")
  void updateRejectsInvalidPersonBeforeCheckingExistence(String description, PersonDto personDto) {
    assertThatThrownBy(() -> personService.update(1L, personDto))
        .isInstanceOf(ConstraintViolationException.class);
    verifyNoInteractions(personRepository);
  }

  @Test
  void createAcceptsMaxLengthNamesAndNullDateOfBirth() {
    when(personRepository.save(any(Person.class))).thenAnswer(invocation -> invocation.getArgument(0));

    assertThatCode(() -> personService.create(personDto(MAX_LENGTH_NAME, MAX_LENGTH_NAME)))
        .doesNotThrowAnyException();
  }

  @Test
  void updateAcceptsMaxLengthNamesAndNullDateOfBirth() {
    when(personRepository.existsById(1L)).thenReturn(true);
    when(personRepository.save(any(Person.class))).thenAnswer(invocation -> invocation.getArgument(0));

    assertThatCode(() -> personService.update(1L, personDto(MAX_LENGTH_NAME, MAX_LENGTH_NAME)))
        .doesNotThrowAnyException();
  }

  private static PersonDto personDto(String firstName, String lastName) {
    PersonDto personDto = new PersonDto();
    personDto.setFirstName(firstName);
    personDto.setLastName(lastName);
    return personDto;
  }

}
