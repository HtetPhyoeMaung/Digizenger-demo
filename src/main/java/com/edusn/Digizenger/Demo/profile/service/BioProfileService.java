package com.edusn.Digizenger.Demo.profile.service;

import com.edusn.Digizenger.Demo.auth.dto.response.Response;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;

public interface BioProfileService {

    ResponseEntity<Response> uploadBio(String bio, HttpServletRequest request);

    ResponseEntity<Response> removeBio(HttpServletRequest request);
}
