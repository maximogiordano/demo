package com.example.demo.converter;

import com.example.demo.dto.PersonDto;
import com.example.demo.model.Person;
import org.springframework.beans.BeanUtils;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class PersonToPersonDtoConverter implements Converter<Person, PersonDto> {
    @Override
    public PersonDto convert(Person source) {
        PersonDto target = new PersonDto();
        BeanUtils.copyProperties(source, target);
        return target;
    }
}
