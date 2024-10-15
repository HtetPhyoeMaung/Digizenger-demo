package com.edusn.Digizenger.Demo.exception.dashboard.user.service;


import com.edusn.Digizenger.Demo.auth.dto.response.Response;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.io.IOException;

public interface ShowUserDashBoardService {

    ResponseEntity<Response> ShowUserDashBoard(String profileUrl, HttpServletRequest request);



}
