package com.example.demo.converter;

import com.example.demo.TestUtils;
import com.example.demo.dto.PersonDto;
import com.example.demo.model.Person;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PersonDtoToPersonConverterTest {
    @Test
    void convert() {
        PersonDto source = TestUtils.createPersonDto(1L, "JOHN", "DOE");
        Person expected = TestUtils.createPerson(1L, "JOHN", "DOE");
        PersonDtoToPersonConverter personDtoToPersonConverter = new PersonDtoToPersonConverter();
        Person result = personDtoToPersonConverter.convert(source);
        assertEquals(expected, result);
    }
}
