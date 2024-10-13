package com.edusn.Digizenger.Demo.checkUser.controller;

import com.edusn.Digizenger.Demo.auth.dto.response.Response;
import com.edusn.Digizenger.Demo.checkUser.service.CheckUserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/check")
public class CheckUserController {

    private final CheckUserService checkUserService;

    @GetMapping("/user")
    public ResponseEntity<Response> checkUser(HttpServletRequest request){
        return checkUserService.checkUser(request);
    }
}
