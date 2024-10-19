package com.edusn.Digizenger.Demo.profile.service.about;

import com.edusn.Digizenger.Demo.auth.dto.response.Response;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;

public interface AboutCareerHistoryService {

    ResponseEntity<Response> uploadCareerHistory(HttpServletRequest request,
                                                    String companyName,
                                                    MultipartFile logoImage,
                                                    String designation,
                                                    LocalDate joinDate,
                                                    LocalDate endDate) throws IOException;

    ResponseEntity<Response> updateCareerHistory(HttpServletRequest request,
                                                    Long careerHistoryId,
                                                    String companyName,
                                                    String designation,
                                                    MultipartFile logoImage,
                                                    LocalDate joinDate,
                                                    LocalDate endDate) throws IOException;

    ResponseEntity<Response> removeCareerHistory(HttpServletRequest request, Long careerHistoryId);

    ResponseEntity<Response> careerHistoryGetById(HttpServletRequest request,Long careerHistoryId);
}
