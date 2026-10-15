package com.example.demo.dto;

import com.example.demo.entity.Person;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import lombok.Data;

@JsonPropertyOrder({"id", "first_name", "last_name", "date_of_birth"})
@Data
public class PersonDto {

  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  private Long id;

  @JsonProperty("first_name")
  @NotNull
  @Size(max = Person.MAX_NAME_LENGTH)
  private String firstName;

  @JsonProperty("last_name")
  @NotNull
  @Size(max = Person.MAX_NAME_LENGTH)
  private String lastName;

  @JsonProperty("date_of_birth")
  private LocalDate dateOfBirth;

}
