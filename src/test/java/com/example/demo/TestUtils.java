package com.example.demo;

import com.example.demo.dto.ErrorDto;
import com.example.demo.dto.PersonDto;
import com.example.demo.model.Person;

public class TestUtils {
    public static Person createPerson(Long id, String firstName, String lastName) {
        Person person = new Person();

        person.setId(id);
        person.setFirstName(firstName);
        person.setLastName(lastName);

        return person;
    }

    public static PersonDto createPersonDto(Long id, String firstName, String lastName) {
        PersonDto personDto = new PersonDto();

        personDto.setId(id);
        personDto.setFirstName(firstName);
        personDto.setLastName(lastName);

        return personDto;
    }

    public static ErrorDto createErrorDto(String message) {
        ErrorDto errorDto = new ErrorDto();

        errorDto.setMessage(message);

        return errorDto;
    }
}
