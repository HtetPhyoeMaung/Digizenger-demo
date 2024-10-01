package com.edusn.Digizenger.Demo.profile.service;

import com.edusn.Digizenger.Demo.auth.dto.response.Response;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;

public interface ProfileImageService {

    ResponseEntity<Response> uploadProfileImage(MultipartFile file, HttpServletRequest request) throws IOException;

    ResponseEntity<Response> deleteProfileImage(HttpServletRequest request);

    ResponseEntity<Response> updateProfileImage(MultipartFile file, HttpServletRequest request) throws IOException;

}
