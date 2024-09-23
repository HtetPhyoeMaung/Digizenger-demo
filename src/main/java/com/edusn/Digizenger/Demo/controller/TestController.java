package com.edusn.Digizenger.Demo.controller;

import com.edusn.Digizenger.Demo.dto.request.LoginRequest;
import com.edusn.Digizenger.Demo.dto.request.RegisterRequest;
import com.edusn.Digizenger.Demo.dto.response.Response;
import com.edusn.Digizenger.Demo.service.auth.*;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/digizenger/api/test/auth")
public class TestController {
    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<Response> register(@RequestBody RegisterRequest request) throws MessagingException {
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
