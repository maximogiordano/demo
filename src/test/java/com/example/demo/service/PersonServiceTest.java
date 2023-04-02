package com.example.demo.service;

import com.example.demo.TestUtils;
import com.example.demo.dto.PersonDto;
import com.example.demo.model.Person;
import com.example.demo.repository.PersonRepository;
import org.junit.jupiter.api.Test;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.domain.*;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class PersonServiceTest {
    @Test
    void create() {
        // mocks
        ConversionService conversionService = mock(ConversionService.class);
        PersonRepository personRepository = mock(PersonRepository.class);

        // sut
        PersonService personService = new PersonService(conversionService, personRepository);

        // data
        Long id = 1L;
        String firstName = "JOHN";
        String lastName = "DOE";
        PersonDto inputData = TestUtils.createPersonDto(null, firstName, lastName);
        Person inputDataAsPerson = TestUtils.createPerson(null, firstName, lastName);
        Person createdPerson = TestUtils.createPerson(id, firstName, lastName);
        PersonDto createdPersonAsDto = TestUtils.createPersonDto(id, firstName, lastName);

        // simulated behavior
        when(conversionService.convert(inputData, Person.class)).thenReturn(inputDataAsPerson);
        when(personRepository.save(inputDataAsPerson)).thenReturn(createdPerson);
        when(conversionService.convert(createdPerson, PersonDto.class)).thenReturn(createdPersonAsDto);

        // action
        PersonDto outputData = personService.create(inputData);

        // verifications
        verify(conversionService).convert(inputData, Person.class);
        verify(personRepository).save(inputDataAsPerson);
        verify(conversionService).convert(createdPerson, PersonDto.class);
        assertEquals(createdPersonAsDto, outputData);
    }

    @Test
    void readPage() {
        // mocks
        ConversionService conversionService = mock(ConversionService.class);
        PersonRepository personRepository = mock(PersonRepository.class);

        // sut
        PersonService personService = new PersonService(conversionService, personRepository);

        // data
        int requestedPage = 0;
        int requestedSize = 10;
        Pageable request = PageRequest.of(requestedPage, requestedSize, Sort.by("id"));
        Long id = 1L;
        String firstName = "JOHN";
        String lastName = "DOE";
        Person storedPerson = TestUtils.createPerson(id, firstName, lastName);
        Page<Person> storedPersonPage = new PageImpl<>(List.of(storedPerson));
        PersonDto storedPersonAsDto = TestUtils.createPersonDto(id, firstName, lastName);
        Page<PersonDto> expectedResult = new PageImpl<>(List.of(storedPersonAsDto));

        // simulated behaviour
        when(personRepository.findAll(request)).thenReturn(storedPersonPage);
        when(conversionService.convert(storedPerson, PersonDto.class)).thenReturn(storedPersonAsDto);

        // action
        Page<PersonDto> result = personService.read(requestedPage, requestedSize);

        // verifications
        verify(personRepository).findAll(request);
        verify(conversionService).convert(storedPerson, PersonDto.class);
        assertEquals(expectedResult, result);
    }

    @Test
    void readOne() {
        // mocks
        ConversionService conversionService = mock(ConversionService.class);
        PersonRepository personRepository = mock(PersonRepository.class);

        // sut
        PersonService personService = new PersonService(conversionService, personRepository);

        // data
        Long id = 1L;
        String firstName = "JOHN";
        String lastName = "DOE";
        Person storedPerson = TestUtils.createPerson(id, firstName, lastName);
        PersonDto storedPersonAsDto = TestUtils.createPersonDto(id, firstName, lastName);

        // simulated behavior
        when(personRepository.findById(id)).thenReturn(Optional.of(storedPerson));
        when(conversionService.convert(storedPerson, PersonDto.class)).thenReturn(storedPersonAsDto);

        // action
        PersonDto result = personService.read(id);

        // verifications
        verify(personRepository).findById(id);
        verify(conversionService).convert(storedPerson, PersonDto.class);
        assertEquals(storedPersonAsDto, result);
    }

    @Test
    void readNone() {
        // mocks
        ConversionService conversionService = mock(ConversionService.class);
        PersonRepository personRepository = mock(PersonRepository.class);

        // sut
        PersonService personService = new PersonService(conversionService, personRepository);

        // data
        Long id = 1L;

        // simulated behavior
        when(personRepository.findById(id)).thenReturn(Optional.empty());

        // actions (includes exceptions verification)
        assertThrows(NoSuchElementException.class, () -> personService.read(id));

        // interactions verifications
        verify(personRepository).findById(id);
        verifyNoInteractions(conversionService);
    }

    @Test
    void update() {
        // mocks
        ConversionService conversionService = mock(ConversionService.class);
        PersonRepository personRepository = mock(PersonRepository.class);

        // sut
        PersonService personService = new PersonService(conversionService, personRepository);

        // data
        Long id = 1L;
        String oldFirstName = "JOHN";
        String oldLastName = "DOE";
        String newFirstName = "MARY";
        String newLastName = "JANE";
        PersonDto inputData = TestUtils.createPersonDto(null, newFirstName, newLastName);
        Person storedPerson = TestUtils.createPerson(id, oldFirstName, oldLastName);
        Person updatedPerson = TestUtils.createPerson(id, newFirstName, newLastName);
        PersonDto updatedPersonAsDto = TestUtils.createPersonDto(id, newFirstName, newLastName);

        // simulated behavior
        when(personRepository.findById(id)).thenReturn(Optional.of(storedPerson));
        when(personRepository.save(updatedPerson)).thenReturn(updatedPerson);
        when(conversionService.convert(updatedPerson, PersonDto.class)).thenReturn(updatedPersonAsDto);

        // action
        PersonDto result = personService.update(id, inputData);

        // verifications
        verify(personRepository).findById(id);
        verify(personRepository).save(updatedPerson);
        verify(conversionService).convert(updatedPerson, PersonDto.class);
        assertEquals(updatedPersonAsDto, result);
    }

    @Test
    void failedUpdate() {
        // mocks
        ConversionService conversionService = mock(ConversionService.class);
        PersonRepository personRepository = mock(PersonRepository.class);

        // sut
        PersonService personService = new PersonService(conversionService, personRepository);

        // data
        Long id = 1L;
        String newFirstName = "MARY";
        String newLastName = "JANE";
        PersonDto inputData = TestUtils.createPersonDto(null, newFirstName, newLastName);

        // simulated behavior
        when(personRepository.findById(id)).thenReturn(Optional.empty());

        // actions (includes exceptions verification)
        assertThrows(NoSuchElementException.class, () -> personService.update(id, inputData));

        // verifications
        verify(personRepository).findById(id);
        verifyNoMoreInteractions(personRepository);
        verifyNoInteractions(conversionService);
    }

    @Test
    void delete() {
        // mocks
        ConversionService conversionService = mock(ConversionService.class);
        PersonRepository personRepository = mock(PersonRepository.class);

        // sut
        PersonService personService = new PersonService(conversionService, personRepository);

        // data
        Long id = 1L;

        // action
        personService.delete(id);

        // verifications
        verify(personRepository).deleteById(id);
    }
}
