package com.edusn.Digizenger.Demo.dto.request;

import lombok.Data;

@Data
public class LoginRequest {
    private String emailOrPhone;
    private String password;
}
