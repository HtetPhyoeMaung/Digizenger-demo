package com.edusn.Digizenger.Demo.auth.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class CustomErrorResponse {
    private int status;
    private String message;
    private List<String> errors; // List to hold individual error messages
    private LocalDateTime timeStamp;
}
