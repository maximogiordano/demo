package com.example.demo.dto;

import com.example.demo.model.Person;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Optional;

@Data
@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class PersonPatchDto {
    private Optional<@Size(max = Person.FIRST_NAME_LENGTH) String> firstName;

    private Optional<@Size(max = Person.LAST_NAME_LENGTH) String> lastName;
}
