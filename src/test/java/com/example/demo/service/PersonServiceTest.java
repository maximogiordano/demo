package com.example.demo.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
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
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@ExtendWith(MockitoExtension.class)
class PersonServiceTest {

  private static final LocalDate DATE_OF_BIRTH = LocalDate.of(1990, 5, 17);

  @Mock
  private PersonRepository personRepository;

  @InjectMocks
  private PersonService personService;

  @Captor
  private ArgumentCaptor<Person> personCaptor;

  @Test
  void createSavesMappedEntityAndReturnsSavedPerson() {
    when(personRepository.save(any(Person.class))).thenReturn(person(1L, "John", "Doe"));

    PersonDto result = personService.create(personDto(null, "John", "Doe"));

    verify(personRepository).save(personCaptor.capture());
    Person saved = personCaptor.getValue();
    assertThat(saved.getId()).isNull();
    assertThat(saved.getFirstName()).isEqualTo("John");
    assertThat(saved.getLastName()).isEqualTo("Doe");
    assertThat(saved.getDateOfBirth()).isEqualTo(DATE_OF_BIRTH);

    assertThat(result).isEqualTo(personDto(1L, "John", "Doe"));
  }

  @Test
  void readByIdReturnsMappedPerson() {
    when(personRepository.findById(1L)).thenReturn(Optional.of(person(1L, "John", "Doe")));

    assertThat(personService.read(1L)).isEqualTo(personDto(1L, "John", "Doe"));
  }

  @Test
  void readByIdThrowsWhenPersonDoesNotExist() {
    when(personRepository.findById(1L)).thenReturn(Optional.empty());

    assertThatThrownBy(() -> personService.read(1L)).isInstanceOf(NoSuchElementException.class);
  }

  @Test
  void readPageMapsContentAndKeepsPagingInfo() {
    Pageable pageable = PageRequest.of(1, 2, Sort.by("lastName"));
    List<Person> persons = List.of(person(3L, "John", "Doe"), person(4L, "Jane", "Roe"));
    when(personRepository.findAll(pageable)).thenReturn(new PageImpl<>(persons, pageable, 5));

    Page<PersonDto> result = personService.read(pageable);

    assertThat(result.getContent())
        .containsExactly(personDto(3L, "John", "Doe"), personDto(4L, "Jane", "Roe"));
    assertThat(result.getPageable()).isEqualTo(pageable);
    assertThat(result.getTotalElements()).isEqualTo(5);
    assertThat(result.getTotalPages()).isEqualTo(3);
  }

  @Test
  void updateThrowsWhenPersonDoesNotExist() {
    when(personRepository.existsById(1L)).thenReturn(false);

    assertThatThrownBy(() -> personService.update(1L, personDto(null, "John", "Doe")))
        .isInstanceOf(NoSuchElementException.class);
    verify(personRepository, never()).save(any());
  }

  @Test
  void updateSavesEntityWithPathIdAndReturnsSavedPerson() {
    when(personRepository.existsById(1L)).thenReturn(true);
    when(personRepository.save(any(Person.class))).thenReturn(person(1L, "Jane", "Doe"));

    PersonDto result = personService.update(1L, personDto(99L, "Jane", "Doe"));

    verify(personRepository).save(personCaptor.capture());
    Person saved = personCaptor.getValue();
    assertThat(saved.getId()).isEqualTo(1L);
    assertThat(saved.getFirstName()).isEqualTo("Jane");
    assertThat(saved.getLastName()).isEqualTo("Doe");
    assertThat(saved.getDateOfBirth()).isEqualTo(DATE_OF_BIRTH);

    assertThat(result).isEqualTo(personDto(1L, "Jane", "Doe"));
  }

  @Test
  void deleteDelegatesToRepository() {
    personService.delete(1L);

    verify(personRepository).deleteById(1L);
  }

  private static Person person(Long id, String firstName, String lastName) {
    Person person = new Person();
    person.setId(id);
    person.setFirstName(firstName);
    person.setLastName(lastName);
    person.setDateOfBirth(DATE_OF_BIRTH);
    return person;
  }

  private static PersonDto personDto(Long id, String firstName, String lastName) {
    PersonDto personDto = new PersonDto();
    personDto.setId(id);
    personDto.setFirstName(firstName);
    personDto.setLastName(lastName);
    personDto.setDateOfBirth(DATE_OF_BIRTH);
    return personDto;
  }

}
