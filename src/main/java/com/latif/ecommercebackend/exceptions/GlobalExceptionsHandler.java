package com.latif.ecommercebackend.exceptions;

import com.latif.ecommercebackend.model.ResponseWrapper;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.support.HttpRequestHandlerServlet;

import java.util.*;

@RestControllerAdvice
public class GlobalExceptionsHandler {


    // validation
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseWrapper> validationException(MethodArgumentNotValidException exception) {

        List<ValidationError> validationErrorList = new ArrayList<>();

        exception.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String fieldMessage = error.getDefaultMessage();
            Object rejectedValue = ((FieldError) error).getRejectedValue();
            ValidationError errors = ValidationError.builder()
                    .errorField(fieldName)
                    .reason(fieldMessage)
                    .rejectedValue(rejectedValue)
                    .build();
            validationErrorList.add(errors);

        });

        return new ResponseEntity<>(
                ResponseWrapper.builder()
                        .success(false)
                        .status(HttpStatus.CONFLICT)
                        .message("Validation error")
                        .data(validationErrorList)
                        .build(),
                HttpStatus.CONFLICT);
    }

    // custom exception

    @ExceptionHandler(EcommerceProjectException.class)
    public ResponseEntity<ResponseWrapper> ecommerceProjectException(EcommerceProjectException exception) {
        return new ResponseEntity<>(
                ResponseWrapper.builder()
                        .message(exception.getMessage())
                        .status(HttpStatus.CONFLICT)
                        .success(false)
                        .build(),
                HttpStatus.CONFLICT
        );
    }

    @ExceptionHandler(UserNotVerifiedException.class)
    public ResponseEntity<ResponseWrapper> userNotVerifiedException(UserNotVerifiedException exception) {

        return new ResponseEntity<>(
                ResponseWrapper.builder()
                        .message((exception.isNewEmailSent())? "User not verified, Email resent": "User not verified")
                        .status(HttpStatus.CONFLICT)
                        .success(false)
                        .build(),
                HttpStatus.CONFLICT
        );
    }

    @ExceptionHandler(EmailFailureException.class)
    public ResponseEntity<ResponseWrapper> emailFailureException(EmailFailureException exception) {

        return new ResponseEntity<>(
                ResponseWrapper.builder()
                        .status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .message(exception.getMessage())
                        .build(),
                HttpStatus.CONFLICT
        );
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ResponseWrapper> userNotFoundException(UserNotFoundException exception) {

        return new ResponseEntity<>(
                ResponseWrapper.builder()
                        .status(HttpStatus.NOT_FOUND)
                        .message(exception.getMessage())
                        .build(),
                HttpStatus.CONFLICT
        );
    }

}
