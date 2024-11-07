package com.edusn.Digizenger.Demo.exception;

import com.amazonaws.services.kms.model.AlreadyExistsException;
import com.edusn.Digizenger.Demo.auth.dto.response.CustomErrorResponse;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.server.NotAcceptableStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(AlreadyExistsException.class)
    public ResponseEntity<CustomErrorResponse> emailExistHandler(AlreadyExistsException ex){
        CustomErrorResponse response = CustomErrorResponse.builder()
                .status(HttpStatus.ALREADY_REPORTED.value())
                .message(ex.getMessage())
                .timeStamp(LocalDateTime.now())
        .build();
        return new ResponseEntity<>(response,HttpStatus.ALREADY_REPORTED);
    }

    @ExceptionHandler(CustomNotFoundException.class)
    public ResponseEntity<CustomErrorResponse> customNotFoundHandler(CustomNotFoundException ex){
        CustomErrorResponse response = CustomErrorResponse.builder()
                .status(HttpStatus.NOT_FOUND.value())
                .message(ex.getMessage())
                .timeStamp(LocalDateTime.now())
                .build();
        return new ResponseEntity<>(response,HttpStatus.NOT_FOUND);
    }


    @ExceptionHandler(NotAcceptableStatusException.class)
    public ResponseEntity<CustomErrorResponse> cannotFollowHandler(NotAcceptableStatusException ex){
        CustomErrorResponse response = CustomErrorResponse.builder()
                .status(HttpStatus.NOT_ACCEPTABLE.value())
                .message(ex.getMessage())
                .timeStamp(LocalDateTime.now())
                .build();
        return new ResponseEntity<>(response,HttpStatus.NOT_ACCEPTABLE);
    }



    @ExceptionHandler(Exception.class)
    public ResponseEntity<CustomErrorResponse> badRequestHandler(Exception ex){

        CustomErrorResponse response = CustomErrorResponse.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .message(ex.getMessage())
                .timeStamp(LocalDateTime.now())
                .build();

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ApiValidationException.class)
    public ResponseEntity<CustomErrorResponse> handleApiValidationException(ApiValidationException ex) {
        CustomErrorResponse response = CustomErrorResponse.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .message("Validation failed")
                .errors(ex.getValidationMessages())
                .timeStamp(LocalDateTime.now())
                .build();
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<CustomErrorResponse> handleValidationExceptions(ConstraintViolationException ex) {
        List<String> errors = ex.getConstraintViolations().stream()
                .map(violation -> violation.getPropertyPath() + ": " + violation.getMessage())
                .collect(Collectors.toList());

        CustomErrorResponse response = CustomErrorResponse.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .message("Validation failed")
                .errors(errors)
                .timeStamp(LocalDateTime.now())
                .build();


        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

}
