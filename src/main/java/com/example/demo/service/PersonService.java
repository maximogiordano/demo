package com.example.demo.service;

import com.example.demo.dto.PersonDto;
import com.example.demo.model.Person;
import com.example.demo.repository.PersonRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.Objects;

@Slf4j
@Service
@Validated
@RequiredArgsConstructor
public class PersonService {
    private final ConversionService conversionService;
    private final PersonRepository personRepository;

    public PersonDto create(@Valid PersonDto personDto) {
        log.info("PersonService.create {}", personDto);
        Person person = Objects.requireNonNull(conversionService.convert(personDto, Person.class));
        return conversionService.convert(personRepository.save(person), PersonDto.class);
    }

    public Page<PersonDto> read(int page, int size) {
        log.info("PersonService.read {} {}", page, size);
        return personRepository.findAll(PageRequest.of(page, size, Sort.by("id")))
                .map(person -> conversionService.convert(person, PersonDto.class));
    }

    public PersonDto read(Long id) {
        log.info("PersonService.read {}", id);
        return personRepository.findById(id)
                .map(person -> conversionService.convert(person, PersonDto.class))
                .orElseThrow();
    }

    public PersonDto update(Long id, @Valid PersonDto personDto) {
        log.info("PersonService.update {} {}", id, personDto);
        Person person = personRepository.findById(id).orElseThrow();
        person.setFirstName(personDto.getFirstName());
        person.setLastName(personDto.getLastName());
        person = personRepository.save(person);
        return conversionService.convert(person, PersonDto.class);
    }

    public void delete(Long id) {
        log.info("PersonService.delete {}", id);
        personRepository.deleteById(id);
    }
}
