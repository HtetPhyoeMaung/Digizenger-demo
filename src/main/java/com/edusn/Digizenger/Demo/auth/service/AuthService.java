package com.edusn.Digizenger.Demo.auth.service;

import com.edusn.Digizenger.Demo.auth.dto.request.LoginRequest;
import com.edusn.Digizenger.Demo.auth.dto.request.RegisterRequest;
import com.edusn.Digizenger.Demo.auth.dto.response.Response;
import com.edusn.Digizenger.Demo.auth.entity.User;
import jakarta.mail.MessagingException;
import org.springframework.http.ResponseEntity;

import java.util.Optional;


public interface AuthService {

    ResponseEntity<Response> register(RegisterRequest request) throws MessagingException;

    ResponseEntity<Response> verifyAccount(String email, String otp);

    ResponseEntity<Response> resendCode(String email) throws MessagingException;

    ResponseEntity<Response> login(LoginRequest request);

    Optional<User> findById(Long recipientId);

    ResponseEntity<Response> disconnect(User user);
}
