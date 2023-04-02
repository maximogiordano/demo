package com.example.demo.converter;

import com.example.demo.dto.PersonDto;
import com.example.demo.model.Person;
import org.springframework.beans.BeanUtils;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class PersonDtoToPersonConverter implements Converter<PersonDto, Person> {
    @Override
    public Person convert(PersonDto source) {
        Person target = new Person();
        BeanUtils.copyProperties(source, target);
        return target;
    }
}
