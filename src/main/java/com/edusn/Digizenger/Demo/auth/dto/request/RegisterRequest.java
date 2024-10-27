package com.edusn.Digizenger.Demo.auth.dto.request;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterRequest {
    @NotBlank(message = "First Name is required!")
    private String firstName;

    @NotBlank(message = "Last Name is required!")
    private String lastName;

    private String email;

    private String phone;

    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters long")
    private String password;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateOfBirth;
    @NotBlank(message = "Gender is required!")
    private String gender;

    @NotBlank(message = "Country is required!")
    private String country;

    @NotBlank(message = "City is required!")
    private String city;

    private String role;

   }
