package com.example.demo.controller;

import com.example.demo.TestUtils;
import com.example.demo.dto.PersonDto;
import com.example.demo.service.PersonService;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class PersonControllerTest {
    @Test
    void create() {
        // mocks
        PersonService personService = mock(PersonService.class);

        // sut
        PersonController personController = new PersonController(personService);

        // data
        Long id = 1L;
        String firstName = "JOHN";
        String lastName = "DOE";
        PersonDto input = TestUtils.createPersonDto(null, firstName, lastName);
        PersonDto output = TestUtils.createPersonDto(id, firstName, lastName);

        // simulated behavior
        when(personService.create(input)).thenReturn(output);

        // action
        PersonDto result = personController.create(input);

        // verifications
        verify(personService).create(input);
        assertEquals(output, result);
    }

    @Test
    void readPage() {
        // mocks
        PersonService personService = mock(PersonService.class);

        // sut
        PersonController personController = new PersonController(personService);

        // data
        int page = 0;
        int size = 10;
        Long id = 1L;
        String firstName = "JOHN";
        String lastName = "DOE";
        PersonDto personDto = TestUtils.createPersonDto(id, firstName, lastName);
        Page<PersonDto> personDtoPage = new PageImpl<>(List.of(personDto));

        // simulated behavior
        when(personService.read(page, size)).thenReturn(personDtoPage);

        // action
        Page<PersonDto> result = personController.read(page, size);

        // verifications
        verify(personService).read(page, size);
        assertEquals(personDtoPage, result);
    }

    @Test
    void readOne() {
        // mocks
        PersonService personService = mock(PersonService.class);

        // sut
        PersonController personController = new PersonController(personService);

        // data
        Long id = 1L;
        String firstName = "JOHN";
        String lastName = "DOE";
        PersonDto personDto = TestUtils.createPersonDto(id, firstName, lastName);

        // simulated behavior
        when(personService.read(id)).thenReturn(personDto);

        // action
        PersonDto result = personController.read(id);

        // verifications
        verify(personService).read(id);
        assertEquals(personDto, result);
    }

    @Test
    void update() {
        // mocks
        PersonService personService = mock(PersonService.class);

        // sut
        PersonController personController = new PersonController(personService);

        // data
        Long id = 1L;
        String firstName = "MARY";
        String lastName = "JANE";
        PersonDto input = TestUtils.createPersonDto(null, firstName, lastName);
        PersonDto output = TestUtils.createPersonDto(id, firstName, lastName);

        // simulated behavior
        when(personService.update(id, input)).thenReturn(output);

        // action
        PersonDto result = personController.update(id, input);

        // verifications
        verify(personService).update(id, input);
        assertEquals(output, result);
    }

    @Test
    void delete() {
        // mocks
        PersonService personService = mock(PersonService.class);

        // sut
        PersonController personController = new PersonController(personService);

        // data
        Long id = 1L;

        // action
        personController.deleteById(id);

        // verifications
        verify(personService).delete(id);
    }
}
