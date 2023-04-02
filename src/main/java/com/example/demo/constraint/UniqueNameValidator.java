package com.example.demo.constraint;

import com.example.demo.dto.PersonDto;
import com.example.demo.repository.PersonRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UniqueNameValidator implements ConstraintValidator<UniqueNameConstraint, PersonDto> {
    private final PersonRepository personRepository;

    @Override
    public boolean isValid(PersonDto value, ConstraintValidatorContext context) {
        return value == null || value.getFirstName() == null || value.getLastName() == null ||
                personRepository.findByFirstNameAndLastName(value.getFirstName(), value.getLastName()).isEmpty();
    }
}
