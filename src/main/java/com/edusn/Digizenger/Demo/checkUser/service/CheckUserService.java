package com.edusn.Digizenger.Demo.checkUser.service;

import com.edusn.Digizenger.Demo.auth.dto.response.Response;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;

public interface CheckUserService {
    ResponseEntity<Response> checkUser(HttpServletRequest request);
}
