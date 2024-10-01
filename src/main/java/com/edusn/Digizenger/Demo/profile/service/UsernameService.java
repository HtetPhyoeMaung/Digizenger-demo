package com.edusn.Digizenger.Demo.profile.service;

import com.edusn.Digizenger.Demo.auth.dto.response.Response;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;

public interface UsernameService {

    ResponseEntity<Response> uploadUsername(String username, HttpServletRequest request);

    ResponseEntity<Response> removeUsername(HttpServletRequest request);
}
