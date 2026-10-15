package com.example.demo.service;

import com.example.demo.dto.PersonDto;
import com.example.demo.entity.Person;
import com.example.demo.repository.PersonRepository;
import jakarta.validation.Valid;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@Validated
@RequiredArgsConstructor
public class PersonService {

  private final PersonRepository personRepository;

  public PersonDto create(@Valid PersonDto personDto) {
    return toPersonDto(personRepository.save(toPerson(personDto)));
  }

  public PersonDto read(Long id) {
    return personRepository.findById(id).map(PersonService::toPersonDto).orElseThrow();
  }

  public Page<PersonDto> read(Pageable pageable) {
    return personRepository.findAll(pageable).map(PersonService::toPersonDto);
  }

  public PersonDto update(Long id, @Valid PersonDto personDto) {
    if (!personRepository.existsById(id)) {
      throw new NoSuchElementException("No value present");
    }

    Person person = toPerson(personDto);
    person.setId(id);
    return toPersonDto(personRepository.save(person));
  }

  public void delete(Long id) {
    personRepository.deleteById(id);
  }

  private static Person toPerson(PersonDto personDto) {
    Person person = new Person();
    BeanUtils.copyProperties(personDto, person);
    return person;
  }

  private static PersonDto toPersonDto(Person person) {
    PersonDto personDto = new PersonDto();
    BeanUtils.copyProperties(person, personDto);
    return personDto;
  }

}
