package com.edusn.Digizenger.Demo.profile.service.about;

import com.edusn.Digizenger.Demo.auth.dto.response.Response;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

public interface AboutEducationService {

    ResponseEntity<Response> uploadEducationHistory(HttpServletRequest request,
                                             String educationQualificationName,
                                             LocalDate joinDate,
                                             LocalDate endDate,
                                             String academicType,
                                             String name,
                                             MultipartFile logoImage,
                                             String city);

    ResponseEntity<Response> updateEducationHistory(HttpServletRequest request,
                                             Long educationHistoryId,
                                             String educationQualificationName,
                                             LocalDate joinDate,
                                             LocalDate endDate,
                                             String academicType,
                                             String name,
                                             MultipartFile logoImage,
                                             String city);

    ResponseEntity<Response> removeEducationHistory(HttpServletRequest request, Long educationHistoryId);
}
