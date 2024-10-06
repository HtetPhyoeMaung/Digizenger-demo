package com.edusn.Digizenger.Demo.profile.service;

import com.edusn.Digizenger.Demo.auth.dto.response.Response;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;

public interface FollowingService {
    ResponseEntity<Response> getProfileFollowingByPage(int _page,
                                                       int _limit,
                                                       String profileUrl,
                                                       HttpServletRequest request);
}
