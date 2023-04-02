package com.example.demo.controller;

import com.example.demo.TestUtils;
import com.example.demo.dto.ErrorDto;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ErrorControllerTest {
    @Test
    void test() {
        // data
        String message = "internal server error";
        Exception exception = new Exception(message);
        ErrorDto errorDto = TestUtils.createErrorDto("java.lang.Exception: " + message);

        // sut
        ErrorController errorController = new ErrorController();

        // action
        ResponseEntity<ErrorDto> errorDtoResponseEntity = errorController.handleException(exception);

        // verifications
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, errorDtoResponseEntity.getStatusCode());
        assertEquals(errorDto, errorDtoResponseEntity.getBody());
    }
}
