package com.example.demo.constraint;

import com.example.demo.TestUtils;
import com.example.demo.dto.PersonDto;
import com.example.demo.model.Person;
import com.example.demo.repository.PersonRepository;
import jakarta.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class UniqueNameValidatorTest {
    @ParameterizedTest
    @MethodSource("getTestNullValuesArguments")
    void testNullValues(PersonDto personDto) {
        // mocks
        PersonRepository personRepository = mock(PersonRepository.class);
        ConstraintValidatorContext constraintValidatorContext = mock(ConstraintValidatorContext.class);

        // sut
        UniqueNameValidator uniqueNameValidator = new UniqueNameValidator(personRepository);

        // actions
        boolean result = uniqueNameValidator.isValid(personDto, constraintValidatorContext);

        // verifications
        verifyNoInteractions(personRepository);
        assertTrue(result);
    }

    private static Stream<Arguments> getTestNullValuesArguments() {
        return Stream.of(
                Arguments.of((PersonDto) null),
                Arguments.of(TestUtils.createPersonDto(null, null, null)),
                Arguments.of(TestUtils.createPersonDto(null, null, "DOE")),
                Arguments.of(TestUtils.createPersonDto(null, "JOHN", null))
        );
    }

    @Test
    void testNonexistentPerson() {
        // mocks
        PersonRepository personRepository = mock(PersonRepository.class);
        ConstraintValidatorContext constraintValidatorContext = mock(ConstraintValidatorContext.class);

        // data
        String firstName = "JOHN";
        String lastName = "DOE";
        PersonDto personDto = TestUtils.createPersonDto(null, firstName, lastName);

        // sut
        UniqueNameValidator uniqueNameValidator = new UniqueNameValidator(personRepository);

        // expectations
        when(personRepository.findByFirstNameAndLastName(firstName, lastName)).thenReturn(Optional.empty());

        // actions
        boolean result = uniqueNameValidator.isValid(personDto, constraintValidatorContext);

        // verifications
        verify(personRepository).findByFirstNameAndLastName(firstName, lastName);
        assertTrue(result);
    }

    @Test
    void testExistentPerson() {
        // mocks
        PersonRepository personRepository = mock(PersonRepository.class);
        ConstraintValidatorContext constraintValidatorContext = mock(ConstraintValidatorContext.class);

        // data
        Long id = 1L;
        String firstName = "JOHN";
        String lastName = "DOE";
        Person person = TestUtils.createPerson(id, firstName, lastName);
        PersonDto personDto = TestUtils.createPersonDto(null, firstName, lastName);

        // sut
        UniqueNameValidator uniqueNameValidator = new UniqueNameValidator(personRepository);

        // expectations
        when(personRepository.findByFirstNameAndLastName(firstName, lastName)).thenReturn(Optional.of(person));

        // actions
        boolean result = uniqueNameValidator.isValid(personDto, constraintValidatorContext);

        // verifications
        verify(personRepository).findByFirstNameAndLastName(firstName, lastName);
        assertFalse(result);
    }
}
