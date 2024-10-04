package com.edusn.Digizenger.Demo.profile.service.about;

import com.edusn.Digizenger.Demo.auth.dto.response.Response;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;

public interface AboutEducationService {

    ResponseEntity<Response> uploadEducation(HttpServletRequest request,
                                             String type,
                                             String name,
                                             String degreeOrDiplomaName,
                                             String logoName,
                                             LocalDate joinDate,
                                             LocalDate endDate,
                                             String present);

    ResponseEntity<Response> updateEducation(HttpServletRequest request,
                                             Long id,
                                             String type,
                                             String name,
                                             String degreeOrDiplomaName,
                                             String logoName,
                                             LocalDate joinDate,
                                             LocalDate endDate,
                                             String present);

    ResponseEntity<Response> removeEducation(HttpServletRequest request, Long id);
}
