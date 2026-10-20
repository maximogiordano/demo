package com.example.demo.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.demo.dto.PersonDto;
import com.example.demo.service.PersonService;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
class PersonControllerTest {

  @InjectMocks
  PersonController personController;

  @Mock
  PersonService personService;

  @Test
  void create() {
    PersonDto input = new PersonDto();

    input.setFirstName("MAX");
    input.setLastName("JORDAN");
    input.setDateOfBirth(LocalDate.parse("1978-10-26"));

    PersonDto output = new PersonDto();

    output.setId(1L);
    output.setFirstName("MAX");
    output.setLastName("JORDAN");
    output.setDateOfBirth(LocalDate.parse("1978-10-26"));

    when(personService.create(input)).thenReturn(output);

    PersonDto result = personController.create(input);

    verify(personService).create(input);
    assertEquals(output, result);
  }

  @Test
  void readById() {
    PersonDto output = new PersonDto();

    output.setId(1L);
    output.setFirstName("MAX");
    output.setLastName("JORDAN");
    output.setDateOfBirth(LocalDate.parse("1978-10-26"));

    when(personService.read(1L)).thenReturn(output);

    PersonDto result = personController.read(1L);

    verify(personService).read(1L);
    assertEquals(output, result);
  }

  @Test
  void readPage() {
    Pageable pageable = PageRequest.of(0, 10);

    PersonDto person = new PersonDto();

    person.setId(1L);
    person.setFirstName("MAX");
    person.setLastName("JORDAN");
    person.setDateOfBirth(LocalDate.parse("1978-10-26"));

    Page<PersonDto> output = new PageImpl<>(List.of(person), pageable, 1);

    when(personService.read(pageable)).thenReturn(output);

    Page<PersonDto> result = personController.read(pageable);

    verify(personService).read(pageable);
    assertEquals(output, result);
  }

  @Test
  void update() {
    PersonDto input = new PersonDto();

    input.setFirstName("MAX");
    input.setLastName("JORDAN");
    input.setDateOfBirth(LocalDate.parse("1978-10-26"));

    PersonDto output = new PersonDto();

    output.setId(1L);
    output.setFirstName("MAX");
    output.setLastName("JORDAN");
    output.setDateOfBirth(LocalDate.parse("1978-10-26"));

    when(personService.update(1L, input)).thenReturn(output);

    PersonDto result = personController.update(1L, input);

    verify(personService).update(1L, input);
    assertEquals(output, result);
  }

  @Test
  void delete() {
    personController.delete(1L);

    verify(personService).delete(1L);
  }

}
