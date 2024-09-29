package com.edusn.Digizenger.Demo.auth.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class CustomErrorResponse {
    private int status;
    private String message;
    private LocalDateTime timeStamp;
}
