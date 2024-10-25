package com.edusn.Digizenger.Demo.auth.controller;
import org.springframework.validation.annotation.Validated;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import com.edusn.Digizenger.Demo.auth.dto.request.LoginRequest;
import com.edusn.Digizenger.Demo.auth.dto.request.RegisterRequest;
import com.edusn.Digizenger.Demo.auth.dto.response.Response;
import com.edusn.Digizenger.Demo.auth.service.AuthService;
import jakarta.mail.MessagingException;

@Slf4j
@RestController
@CrossOrigin
@RequestMapping("/api/v1/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<Response> register(@RequestBody @Validated RegisterRequest request,BindingResult result) throws MessagingException {
        log.info("Reach Register");
        return authService.register(request);
    }


    @PutMapping("/verify-account")
    public ResponseEntity<Response> verifyAccount(@RequestParam String emailOrPhone, @RequestParam String otp) {
        return authService.verifyAccount(emailOrPhone, otp);
    }


    @PutMapping("/resend-code")
    public ResponseEntity<Response> resendCode(@RequestParam String emailOrPhone) throws MessagingException {
        return authService.resendCode(emailOrPhone);
    }

    @PostMapping("/login")
    public ResponseEntity<Response> login(@RequestBody LoginRequest request) {
        return authService.login(request);
    }

}
