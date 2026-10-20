package com.example.demo.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.demo.dto.PersonDto;
import com.example.demo.entity.Person;
import com.example.demo.repository.PersonRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
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
class PersonServiceTest {

  @InjectMocks
  PersonService personService;

  @Mock
  PersonRepository personRepository;

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

    when(personRepository.save(any())).thenAnswer(invocation -> {
      Person person = invocation.getArgument(0);
      person.setId(1L);
      return person;
    });

    PersonDto result = personService.create(input);

    assertEquals(output, result);
  }

  @Test
  void readById() {
    Person person = new Person();

    person.setId(1L);
    person.setFirstName("MAX");
    person.setLastName("JORDAN");
    person.setDateOfBirth(LocalDate.parse("1978-10-26"));

    PersonDto output = new PersonDto();

    output.setId(1L);
    output.setFirstName("MAX");
    output.setLastName("JORDAN");
    output.setDateOfBirth(LocalDate.parse("1978-10-26"));

    when(personRepository.findById(1L)).thenReturn(Optional.of(person));

    PersonDto result = personService.read(1L);

    assertEquals(output, result);
  }

  @Test
  void readByIdNotFound() {
    when(personRepository.findById(1L)).thenReturn(Optional.empty());

    assertThrows(NoSuchElementException.class, () -> personService.read(1L));
  }

  @Test
  void readPage() {
    Pageable pageable = PageRequest.of(0, 10);

    Person person = new Person();

    person.setId(1L);
    person.setFirstName("MAX");
    person.setLastName("JORDAN");
    person.setDateOfBirth(LocalDate.parse("1978-10-26"));

    PersonDto personDto = new PersonDto();

    personDto.setId(1L);
    personDto.setFirstName("MAX");
    personDto.setLastName("JORDAN");
    personDto.setDateOfBirth(LocalDate.parse("1978-10-26"));

    Page<PersonDto> output = new PageImpl<>(List.of(personDto), pageable, 1);

    when(personRepository.findAll(pageable)).thenReturn(
        new PageImpl<>(List.of(person), pageable, 1));

    Page<PersonDto> result = personService.read(pageable);

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

    when(personRepository.existsById(1L)).thenReturn(true);
    when(personRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

    PersonDto result = personService.update(1L, input);

    assertEquals(output, result);
  }

  @Test
  void updateNotFound() {
    PersonDto input = new PersonDto();

    input.setFirstName("MAX");
    input.setLastName("JORDAN");
    input.setDateOfBirth(LocalDate.parse("1978-10-26"));

    when(personRepository.existsById(1L)).thenReturn(false);

    assertThrows(NoSuchElementException.class, () -> personService.update(1L, input));
    verify(personRepository, never()).save(any());
  }

  @Test
  void delete() {
    personService.delete(1L);

    verify(personRepository).deleteById(1L);
  }

}
