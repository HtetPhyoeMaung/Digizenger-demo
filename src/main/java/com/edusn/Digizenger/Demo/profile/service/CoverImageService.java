package com.edusn.Digizenger.Demo.profile.service;

import com.edusn.Digizenger.Demo.auth.dto.response.Response;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;

public interface CoverImageService {

    ResponseEntity<Response> uploadCoverImage(MultipartFile file, HttpServletRequest request) throws IOException;

    ResponseEntity<Response> deleteCoverImage(HttpServletRequest request);

    ResponseEntity<Response> updateCoverImage(MultipartFile file, HttpServletRequest request) throws IOException;
}
