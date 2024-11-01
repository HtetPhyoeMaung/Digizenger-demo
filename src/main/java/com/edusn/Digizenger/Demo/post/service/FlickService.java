package com.edusn.Digizenger.Demo.post.service;

import com.edusn.Digizenger.Demo.auth.dto.response.Response;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;

public interface FlickService {
    ResponseEntity<Response> flick(int postId, HttpServletRequest request);
}
