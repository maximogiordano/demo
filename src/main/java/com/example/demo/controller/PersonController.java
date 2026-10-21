package com.example.demo.controller;

import com.example.demo.dto.ErrorDto;
import com.example.demo.dto.PersonDto;
import com.example.demo.service.PersonService;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/persons")
// Error responses are produced by GlobalExceptionHandler; each operation lists the ones it can
// return. The class-level one applies to every operation. Operations without @ResponseStatus
// declare their 200 too, since springdoc only generates it when no responses are declared.
@ApiResponse(responseCode = "500", description = "Unexpected error",
    content = @Content(schema = @Schema(implementation = ErrorDto.class)))
@RequiredArgsConstructor
public class PersonController {

  private final PersonService personService;

  @PostMapping
  @ApiResponse(responseCode = "400", description = "Malformed JSON body or validation errors",
      content = @Content(schema = @Schema(implementation = ErrorDto.class)))
  @ResponseStatus(HttpStatus.CREATED)
  public PersonDto create(@RequestBody PersonDto personDto) {
    return personService.create(personDto);
  }

  @GetMapping("/{id}")
  @ApiResponse(responseCode = "200", description = "OK")
  @ApiResponse(responseCode = "400", description = "Unparseable id",
      content = @Content(schema = @Schema(implementation = ErrorDto.class)))
  @ApiResponse(responseCode = "404", description = "Person not found",
      content = @Content(schema = @Schema(implementation = ErrorDto.class)))
  public PersonDto read(@PathVariable Long id) {
    return personService.read(id);
  }

  @GetMapping
  @ApiResponse(responseCode = "200", description = "OK")
  @ApiResponse(responseCode = "400", description = "Sort param specifies a non-existent property",
      content = @Content(schema = @Schema(implementation = ErrorDto.class)))
  public Page<PersonDto> read(Pageable pageable) {
    return personService.read(pageable);
  }

  @PutMapping("/{id}")
  @ApiResponse(responseCode = "200", description = "OK")
  @ApiResponse(responseCode = "400",
      description = "Unparseable id, malformed JSON body or validation errors",
      content = @Content(schema = @Schema(implementation = ErrorDto.class)))
  @ApiResponse(responseCode = "404", description = "Person not found",
      content = @Content(schema = @Schema(implementation = ErrorDto.class)))
  @ApiResponse(responseCode = "409",
      description = "Person deleted by another request during the update",
      content = @Content(schema = @Schema(implementation = ErrorDto.class)))
  public PersonDto update(@PathVariable Long id, @RequestBody PersonDto personDto) {
    return personService.update(id, personDto);
  }

  @DeleteMapping("/{id}")
  @ApiResponse(responseCode = "400", description = "Unparseable id",
      content = @Content(schema = @Schema(implementation = ErrorDto.class)))
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void delete(@PathVariable Long id) {
    personService.delete(id);
  }

}
