package com.edusn.Digizenger.Demo.profile.service;

import com.edusn.Digizenger.Demo.auth.dto.response.Response;
import com.edusn.Digizenger.Demo.auth.entity.User;
import org.springframework.http.ResponseEntity;

public interface ProfileService {
    ResponseEntity<Response> showUserProfile(User user);
}
