package com.latif.ecommercebackend.exceptions;

import com.latif.ecommercebackend.model.ResponseWrapper;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionsHandler {


    // validation
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseWrapper> validationException(MethodArgumentNotValidException se) {

        Map<String, String> errors = new HashMap<>();

        se.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String fieldMessage = error.getDefaultMessage();
            errors.put(fieldName, fieldMessage);
        });

        return new ResponseEntity<>(
                ResponseWrapper.builder()
                        .success(false)
                        .status(HttpStatus.CONFLICT)
                        .message("Validation error")
                        .body(errors)
                        .build(),
                HttpStatus.CONFLICT);
    }

    // custom exception

    @ExceptionHandler(EcommerceProjectException.class)
    public ResponseEntity<ResponseWrapper> ecommerceProjectException(EcommerceProjectException ex){
        return new ResponseEntity<>(
                ResponseWrapper.builder()
                        .message(ex.getMessage())
                        .status(HttpStatus.CONFLICT)
                        .success(false)
                        .build(),
                HttpStatus.CONFLICT
        );
    }


}
