package com.edusn.Digizenger.Demo.profile.service.about;

import com.edusn.Digizenger.Demo.auth.dto.response.Response;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;

public interface CompanyService {
    ResponseEntity<Response> getExistingCompanyNameByName(HttpServletRequest request, String name);

}
