package com.example.demo.exception;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.demo.dto.ErrorDto;
import com.example.demo.entity.Person;
import jakarta.validation.ConstraintViolationException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.springframework.data.core.PropertyReferenceException;
import org.springframework.data.core.TypeInformation;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.mock.http.MockHttpInputMessage;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

class GlobalExceptionHandlerTest {

  private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

  @Test
  void handleNoResourceFoundExceptionReturnsNotFound() {
    NoResourceFoundException e =
        new NoResourceFoundException(HttpMethod.GET, "/unknown", "unknown");

    ResponseEntity<ErrorDto> response = handler.handleNoResourceFoundException(e);

    assertError(response, HttpStatus.NOT_FOUND, e, "the given endpoint does not exist");
  }

  @Test
  void handleHttpRequestMethodNotSupportedExceptionReturnsMethodNotAllowed() {
    HttpRequestMethodNotSupportedException e = new HttpRequestMethodNotSupportedException("PATCH");

    ResponseEntity<ErrorDto> response = handler.handleHttpRequestMethodNotSupportedException(e);

    assertError(response, HttpStatus.METHOD_NOT_ALLOWED, e,
        "the given method is not supported for the specified endpoint");
  }

  @Test
  void handleMethodArgumentTypeMismatchExceptionReturnsBadRequest() {
    MethodArgumentTypeMismatchException e = new MethodArgumentTypeMismatchException(
        "abc", Long.class, "id", null, new NumberFormatException("For input string: \"abc\""));

    ResponseEntity<ErrorDto> response = handler.handleMethodArgumentTypeMismatchException(e);

    assertError(response, HttpStatus.BAD_REQUEST, e, "the given URL contains parsing errors");
  }

  @Test
  void handlePropertyReferenceExceptionReturnsBadRequest() {
    PropertyReferenceException e = new PropertyReferenceException(
        "unknown", TypeInformation.of(Person.class), List.of());

    ResponseEntity<ErrorDto> response = handler.handlePropertyReferenceException(e);

    assertError(response, HttpStatus.BAD_REQUEST, e,
        "the sort param specifies a non-existent property");
  }

  @Test
  void handleHttpMessageNotReadableExceptionReturnsBadRequest() {
    HttpMessageNotReadableException e = new HttpMessageNotReadableException(
        "JSON parse error", new MockHttpInputMessage(new byte[0]));

    ResponseEntity<ErrorDto> response = handler.handleHttpMessageNotReadableException(e);

    assertError(response, HttpStatus.BAD_REQUEST, e, "the request body contains syntax errors");
  }

  @Test
  void handleConstraintViolationExceptionReturnsBadRequest() {
    ConstraintViolationException e =
        new ConstraintViolationException("create.personDto.firstName: must not be blank", Set.of());

    ResponseEntity<ErrorDto> response = handler.handleConstraintViolationException(e);

    assertError(response, HttpStatus.BAD_REQUEST, e,
        "the request body contains validation errors");
  }

  @Test
  void handleNoSuchElementExceptionReturnsNotFound() {
    NoSuchElementException e = new NoSuchElementException("No value present");

    ResponseEntity<ErrorDto> response = handler.handleNoSuchElementException(e);

    assertError(response, HttpStatus.NOT_FOUND, e, "the given object does not exist");
  }

  @Test
  void handleExceptionReturnsInternalServerError() {
    IllegalStateException e = new IllegalStateException("boom");

    ResponseEntity<ErrorDto> response = handler.handleException(e);

    assertError(response, HttpStatus.INTERNAL_SERVER_ERROR, e, "unexpected error");
  }

  @Test
  void handleExceptionKeepsNullDetailWhenMessageIsNull() {
    RuntimeException e = new RuntimeException();

    ResponseEntity<ErrorDto> response = handler.handleException(e);

    assertThat(response.getBody()).isNotNull();
    assertThat(response.getBody().getDetail()).isNull();
  }

  private static void assertError(ResponseEntity<ErrorDto> response, HttpStatus status,
      Exception e, String description) {
    assertThat(response.getStatusCode()).isEqualTo(status);

    ErrorDto body = response.getBody();
    assertThat(body).isNotNull();
    assertThat(body.getType()).isEqualTo(e.getClass().getName());
    assertThat(body.getDescription()).isEqualTo(description);
    assertThat(body.getDetail()).isEqualTo(e.getMessage());
  }

}
