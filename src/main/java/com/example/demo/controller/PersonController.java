package com.example.demo.controller;

import com.example.demo.dto.PersonDto;
import com.example.demo.service.PersonService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/persons")
public class PersonController {
    private final PersonService personService;

    @PostMapping
    public PersonDto create(@RequestBody PersonDto personDto) {
        log.info("POST /persons {}", personDto);
        return personService.create(personDto);
    }

    @GetMapping
    public Page<PersonDto> read(@RequestParam(defaultValue = "0") Integer page,
                                @RequestParam(defaultValue = "10") Integer size) {
        log.info("GET /persons?page={}&size={}", page, size);
        return personService.read(page, size);
    }

    @GetMapping("/{id}")
    public PersonDto read(@PathVariable Long id) {
        log.info("GET /persons/{}", id);
        return personService.read(id);
    }

    @PutMapping("/{id}")
    public PersonDto update(@PathVariable Long id, @RequestBody PersonDto personDto) {
        log.info("PUT /persons/{} {}", id, personDto);
        return personService.update(id, personDto);
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable Long id) {
        log.info("DELETE /persons/{}", id);
        personService.delete(id);
    }
}
