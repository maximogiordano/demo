package com.example.demo.dto;

import com.example.demo.constraint.UniqueNameConstraint;
import com.example.demo.model.Person;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@UniqueNameConstraint
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class PersonDto {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    @NotNull
    @Size(max = Person.FIRST_NAME_LENGTH)
    private String firstName;

    @NotNull
    @Size(max = Person.LAST_NAME_LENGTH)
    private String lastName;
}
