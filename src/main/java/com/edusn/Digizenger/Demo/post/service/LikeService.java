package com.edusn.Digizenger.Demo.post.service;


import com.edusn.Digizenger.Demo.auth.dto.response.Response;
import com.edusn.Digizenger.Demo.auth.entity.User;
import org.springframework.http.ResponseEntity;

public interface LikeService {
    ResponseEntity<Response> isLike(Long id, User user);

}
