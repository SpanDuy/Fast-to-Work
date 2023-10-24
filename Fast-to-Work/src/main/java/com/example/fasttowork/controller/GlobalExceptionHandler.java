package com.example.fasttowork.controller;

import com.example.fasttowork.exception.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<BadRequestException> handleException(BadRequestException badRequestException) {
        return new ResponseEntity<>(badRequestException, HttpStatus.BAD_REQUEST);
    }
}
