package com.example.demo.exception;

import com.example.demo.dto.ErrorDto;
import jakarta.validation.ConstraintViolationException;
import java.util.NoSuchElementException;
import org.springframework.data.core.PropertyReferenceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(NoResourceFoundException.class)
  public ResponseEntity<ErrorDto> handleNoResourceFoundException(NoResourceFoundException e) {
    return error(HttpStatus.NOT_FOUND, "the given endpoint does not exist", e);
  }

  @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
  public ResponseEntity<ErrorDto> handleHttpRequestMethodNotSupportedException(
      HttpRequestMethodNotSupportedException e) {
    return error(HttpStatus.METHOD_NOT_ALLOWED,
        "the given method is not supported for the specified endpoint", e);
  }

  @ExceptionHandler(MethodArgumentTypeMismatchException.class)
  public ResponseEntity<ErrorDto> handleMethodArgumentTypeMismatchException(
      MethodArgumentTypeMismatchException e) {
    return error(HttpStatus.BAD_REQUEST, "the given URL contains parsing errors", e);
  }

  @ExceptionHandler(PropertyReferenceException.class)
  public ResponseEntity<ErrorDto> handlePropertyReferenceException(PropertyReferenceException e) {
    return error(HttpStatus.BAD_REQUEST, "the sort param specifies a non-existent property", e);
  }

  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ResponseEntity<ErrorDto> handleHttpMessageNotReadableException(
      HttpMessageNotReadableException e) {
    return error(HttpStatus.BAD_REQUEST, "the request body contains syntax errors", e);
  }

  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<ErrorDto> handleConstraintViolationException(
      ConstraintViolationException e) {
    return error(HttpStatus.BAD_REQUEST, "the request body contains validation errors", e);
  }

  @ExceptionHandler(NoSuchElementException.class)
  public ResponseEntity<ErrorDto> handleNoSuchElementException(NoSuchElementException e) {
    return error(HttpStatus.NOT_FOUND, "the given object does not exist", e);
  }

  @ExceptionHandler(ObjectOptimisticLockingFailureException.class)
  public ResponseEntity<ErrorDto> handleObjectOptimisticLockingFailureException(
      ObjectOptimisticLockingFailureException e) {
    return error(HttpStatus.CONFLICT, "the given object was modified or deleted by another request",
        e);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorDto> handleException(Exception e) {
    return error(HttpStatus.INTERNAL_SERVER_ERROR, "unexpected error", e);
  }

  private ResponseEntity<ErrorDto> error(HttpStatus status, String description, Exception e) {
    ErrorDto errorDto = new ErrorDto();

    errorDto.setType(e.getClass().getName());
    errorDto.setDescription(description);
    errorDto.setDetail(e.getMessage());

    return new ResponseEntity<>(errorDto, status);
  }

}
