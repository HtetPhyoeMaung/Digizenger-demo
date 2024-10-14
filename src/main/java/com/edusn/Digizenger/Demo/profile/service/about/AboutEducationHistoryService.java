package com.edusn.Digizenger.Demo.profile.service.about;

import com.edusn.Digizenger.Demo.auth.dto.response.Response;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;

public interface AboutEducationHistoryService {

    ResponseEntity<Response> uploadEducationHistory(HttpServletRequest request,
                                                    String schoolName,
                                                    MultipartFile logoImage,
                                                    String degreeName,
                                                    String fieldOfStudy,
                                                    LocalDate joinDate,
                                                    LocalDate endDate) throws IOException;

    ResponseEntity<Response> updateEducationHistory(HttpServletRequest request,
                                                    Long educationHistoryId,
                                                    String schoolName,
                                                    String degreeName,
                                                    String fieldOfStudy,
                                                    MultipartFile logoImage,
                                                    LocalDate joinDate,
                                                    LocalDate endDate) throws IOException;

    ResponseEntity<Response> removeEducationHistory(HttpServletRequest request, Long educationHistoryId);
}
