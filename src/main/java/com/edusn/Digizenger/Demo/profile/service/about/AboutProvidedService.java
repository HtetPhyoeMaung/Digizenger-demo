package com.edusn.Digizenger.Demo.profile.service.about;

import com.edusn.Digizenger.Demo.auth.dto.response.Response;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;

public interface AboutProvidedService {

    ResponseEntity<Response> findByServiceName(HttpServletRequest servletRequest, String service);

    ResponseEntity<Response> uploadServiceProvided(HttpServletRequest request,
                                                   String service);

    ResponseEntity<Response> updateServiceProvided(HttpServletRequest request,
                                                   Long id,
                                                   String service);

    ResponseEntity<Response> removeServiceProvided(HttpServletRequest request,Long id);
}
