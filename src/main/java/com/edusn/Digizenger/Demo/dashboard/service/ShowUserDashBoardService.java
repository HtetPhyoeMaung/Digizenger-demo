package com.edusn.Digizenger.Demo.dashboard.service;


import com.edusn.Digizenger.Demo.auth.dto.response.Response;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;

public interface ShowUserDashBoardService {

    ResponseEntity<Response> ShowUserDashBoard(String profileUrl, HttpServletRequest request);



}
