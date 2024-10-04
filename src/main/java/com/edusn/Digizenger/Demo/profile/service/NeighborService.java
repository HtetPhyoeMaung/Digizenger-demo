package com.edusn.Digizenger.Demo.profile.service;

import com.edusn.Digizenger.Demo.auth.dto.response.Response;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;

public interface NeighborService {
    ResponseEntity<Response> getProfileNeighborsByPage(int _page,
                                                       int _limit,
                                                       String profileUrl,
                                                       HttpServletRequest request);
}
