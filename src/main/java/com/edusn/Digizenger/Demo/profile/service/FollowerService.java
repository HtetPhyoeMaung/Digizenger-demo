package com.edusn.Digizenger.Demo.profile.service;

import com.edusn.Digizenger.Demo.auth.dto.response.Response;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;

public interface FollowerService {

    ResponseEntity<Response> followToProfile(HttpServletRequest request, String toFollowUserProfileUrl);

    ResponseEntity<Response> unFollowToProfile(HttpServletRequest request, String toUnFollowUserProfileUrl);

    ResponseEntity<Response> getProfileFollowersByPage(int _page,
                                                       int _limit,
                                                       String profileUrl,
                                                       HttpServletRequest request);
}
