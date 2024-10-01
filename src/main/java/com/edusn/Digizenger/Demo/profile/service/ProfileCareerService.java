package com.edusn.Digizenger.Demo.profile.service;

import com.edusn.Digizenger.Demo.auth.dto.response.Response;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;

public interface ProfileCareerService {

    ResponseEntity<Response> uploadCareer(String careerName, HttpServletRequest request);

    ResponseEntity<Response> removeCareer(HttpServletRequest request);
}
