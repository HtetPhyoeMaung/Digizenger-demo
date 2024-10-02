package com.edusn.Digizenger.Demo.auth.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.edusn.Digizenger.Demo.auth.dto.request.LoginRequest;
import com.edusn.Digizenger.Demo.auth.dto.request.RegisterRequest;
import com.edusn.Digizenger.Demo.auth.dto.response.Response;
import com.edusn.Digizenger.Demo.auth.service.UserService;

import jakarta.mail.MessagingException;
@Slf4j
@RestController
@CrossOrigin
@RequestMapping("/api/v1/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<Response> register(@RequestBody RegisterRequest request) throws MessagingException {
        log.info("Reach Register");
        return userService.register(request);
    }

    @PutMapping("/verify-account")
    public ResponseEntity<Response> verifyAccount(@RequestParam String emailOrPhone, @RequestParam String otp) {
        return userService.verifyAccount(emailOrPhone, otp);
    }

    @PutMapping("/resend-code")
    public ResponseEntity<Response> resendCode(@RequestParam String emailOrPhone) throws MessagingException {
        return userService.resendCode(emailOrPhone);
    }

    @PostMapping("/login")
    public ResponseEntity<Response> login(@RequestBody LoginRequest request) {
        return userService.login(request);
    }

}
