package com.example.demo.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.example.demo.controller.PersonController;
import com.example.demo.dto.ErrorDto;
import com.example.demo.entity.Person;
import jakarta.validation.ConstraintViolationException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.springframework.core.MethodParameter;
import org.springframework.data.core.PropertyReferenceException;
import org.springframework.data.core.TypeInformation;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.mock.http.MockHttpInputMessage;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

class GlobalExceptionHandlerTest {

  @Test
  void handleNoResourceFoundException() {
    GlobalExceptionHandler globalExceptionHandler = new GlobalExceptionHandler();
    NoResourceFoundException noResourceFoundException = new NoResourceFoundException(HttpMethod.GET,
        "/foo", "foo");

    ErrorDto errorDto = new ErrorDto();

    errorDto.setType(NoResourceFoundException.class.getName());
    errorDto.setDescription("the given endpoint does not exist");
    errorDto.setDetail(noResourceFoundException.getMessage());

    ResponseEntity<ErrorDto> result = globalExceptionHandler.handleNoResourceFoundException(
        noResourceFoundException);

    assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
    assertEquals(errorDto, result.getBody());
  }

  @Test
  void handleHttpRequestMethodNotSupportedException() {
    GlobalExceptionHandler globalExceptionHandler = new GlobalExceptionHandler();
    HttpRequestMethodNotSupportedException httpRequestMethodNotSupportedException = new HttpRequestMethodNotSupportedException(
        "PATCH");

    ErrorDto errorDto = new ErrorDto();

    errorDto.setType(HttpRequestMethodNotSupportedException.class.getName());
    errorDto.setDescription("the given method is not supported for the specified endpoint");
    errorDto.setDetail(httpRequestMethodNotSupportedException.getMessage());

    ResponseEntity<ErrorDto> result = globalExceptionHandler.handleHttpRequestMethodNotSupportedException(
        httpRequestMethodNotSupportedException);

    assertEquals(HttpStatus.METHOD_NOT_ALLOWED, result.getStatusCode());
    assertEquals(errorDto, result.getBody());
  }

  @Test
  void handleMethodArgumentTypeMismatchException() throws NoSuchMethodException {
    GlobalExceptionHandler globalExceptionHandler = new GlobalExceptionHandler();
    MethodParameter methodParameter = new MethodParameter(
        PersonController.class.getMethod("read", Long.class), 0);
    MethodArgumentTypeMismatchException methodArgumentTypeMismatchException = new MethodArgumentTypeMismatchException(
        "foo", Long.class, "id", methodParameter,
        new NumberFormatException("For input string: \"foo\""));

    ErrorDto errorDto = new ErrorDto();

    errorDto.setType(MethodArgumentTypeMismatchException.class.getName());
    errorDto.setDescription("the given URL contains parsing errors");
    errorDto.setDetail(methodArgumentTypeMismatchException.getMessage());

    ResponseEntity<ErrorDto> result = globalExceptionHandler.handleMethodArgumentTypeMismatchException(
        methodArgumentTypeMismatchException);

    assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
    assertEquals(errorDto, result.getBody());
  }

  @Test
  void handlePropertyReferenceException() {
    GlobalExceptionHandler globalExceptionHandler = new GlobalExceptionHandler();
    PropertyReferenceException propertyReferenceException = new PropertyReferenceException("foo",
        TypeInformation.of(Person.class), List.of());

    ErrorDto errorDto = new ErrorDto();

    errorDto.setType(PropertyReferenceException.class.getName());
    errorDto.setDescription("the sort param specifies a non-existent property");
    errorDto.setDetail(propertyReferenceException.getMessage());

    ResponseEntity<ErrorDto> result = globalExceptionHandler.handlePropertyReferenceException(
        propertyReferenceException);

    assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
    assertEquals(errorDto, result.getBody());
  }

  @Test
  void handleHttpMessageNotReadableException() {
    GlobalExceptionHandler globalExceptionHandler = new GlobalExceptionHandler();
    HttpMessageNotReadableException httpMessageNotReadableException = new HttpMessageNotReadableException(
        "JSON parse error", new MockHttpInputMessage(new byte[0]));

    ErrorDto errorDto = new ErrorDto();

    errorDto.setType(HttpMessageNotReadableException.class.getName());
    errorDto.setDescription("the request body contains syntax errors");
    errorDto.setDetail(httpMessageNotReadableException.getMessage());

    ResponseEntity<ErrorDto> result = globalExceptionHandler.handleHttpMessageNotReadableException(
        httpMessageNotReadableException);

    assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
    assertEquals(errorDto, result.getBody());
  }

  @Test
  void handleConstraintViolationException() {
    GlobalExceptionHandler globalExceptionHandler = new GlobalExceptionHandler();
    ConstraintViolationException constraintViolationException = new ConstraintViolationException(
        "create.personDto.firstName: must not be null", Set.of());

    ErrorDto errorDto = new ErrorDto();

    errorDto.setType(ConstraintViolationException.class.getName());
    errorDto.setDescription("the request body contains validation errors");
    errorDto.setDetail(constraintViolationException.getMessage());

    ResponseEntity<ErrorDto> result = globalExceptionHandler.handleConstraintViolationException(
        constraintViolationException);

    assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
    assertEquals(errorDto, result.getBody());
  }

  @Test
  void handleNoSuchElementException() {
    GlobalExceptionHandler globalExceptionHandler = new GlobalExceptionHandler();
    NoSuchElementException noSuchElementException = new NoSuchElementException("No value present");

    ErrorDto errorDto = new ErrorDto();

    errorDto.setType(NoSuchElementException.class.getName());
    errorDto.setDescription("the given object does not exist");
    errorDto.setDetail(noSuchElementException.getMessage());

    ResponseEntity<ErrorDto> result = globalExceptionHandler.handleNoSuchElementException(
        noSuchElementException);

    assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
    assertEquals(errorDto, result.getBody());
  }

  @Test
  void handleObjectOptimisticLockingFailureException() {
    GlobalExceptionHandler globalExceptionHandler = new GlobalExceptionHandler();
    ObjectOptimisticLockingFailureException objectOptimisticLockingFailureException = new ObjectOptimisticLockingFailureException(
        Person.class, 1L);

    ErrorDto errorDto = new ErrorDto();

    errorDto.setType(ObjectOptimisticLockingFailureException.class.getName());
    errorDto.setDescription("the given object was modified or deleted by another request");
    errorDto.setDetail(objectOptimisticLockingFailureException.getMessage());

    ResponseEntity<ErrorDto> result = globalExceptionHandler.handleObjectOptimisticLockingFailureException(
        objectOptimisticLockingFailureException);

    assertEquals(HttpStatus.CONFLICT, result.getStatusCode());
    assertEquals(errorDto, result.getBody());
  }

  @Test
  void handleException() {
    GlobalExceptionHandler globalExceptionHandler = new GlobalExceptionHandler();
    Exception exception = new Exception("foo");

    ErrorDto errorDto = new ErrorDto();

    errorDto.setType(Exception.class.getName());
    errorDto.setDescription("unexpected error");
    errorDto.setDetail(exception.getMessage());

    ResponseEntity<ErrorDto> result = globalExceptionHandler.handleException(exception);

    assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());
    assertEquals(errorDto, result.getBody());
  }

}
