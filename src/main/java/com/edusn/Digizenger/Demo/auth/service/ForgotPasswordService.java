package com.edusn.Digizenger.Demo.auth.service;

import com.edusn.Digizenger.Demo.auth.dto.response.Response;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;

public interface ForgotPasswordService {

    ResponseEntity<Response> checkUserByEmailOrPhoneOrUsername(String emailOrPhoneOrUsername);

    ResponseEntity<Response> resetPassword(HttpServletRequest request, String newPassword, String conformPassword);
}
