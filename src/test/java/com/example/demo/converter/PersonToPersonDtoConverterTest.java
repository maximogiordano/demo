package com.example.demo.converter;

import com.example.demo.TestUtils;
import com.example.demo.dto.PersonDto;
import com.example.demo.model.Person;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PersonToPersonDtoConverterTest {
    @Test
    void convert() {
        Person source = TestUtils.createPerson(1L, "JOHN", "DOE");
        PersonDto expected = TestUtils.createPersonDto(1L, "JOHN", "DOE");
        PersonToPersonDtoConverter personToPersonDtoConverter = new PersonToPersonDtoConverter();
        PersonDto result = personToPersonDtoConverter.convert(source);
        assertEquals(expected, result);
    }
}
