package com.edusn.Digizenger.Demo.dashboard.admin.controller;

import com.edusn.Digizenger.Demo.auth.dto.response.Response;
import com.edusn.Digizenger.Demo.dashboard.admin.service.AdminDashBoardServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/dashboard")
@RequiredArgsConstructor
public class AdminDashBoardController {

    private final AdminDashBoardServiceImpl adminDashBoardService;

    @GetMapping("/admin")
    public ResponseEntity<Response>  getDashBoard(HttpServletRequest  request){

        return  adminDashBoardService.showAdminDashBoard(request);
    }
}
