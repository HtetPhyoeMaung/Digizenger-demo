package com.edusn.Digizenger.Demo.exception;

import lombok.Getter;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class ApiValidationException extends RuntimeException {

    // Getter for all validation messages
    private final List<String> validationMessages;

    public ApiValidationException(BindingResult result) {
        // Collect all validation error messages
        this.validationMessages = result.getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.toList());
    }

    @Override
    public String getMessage() {
        return String.join(", ", validationMessages);
    }
}
