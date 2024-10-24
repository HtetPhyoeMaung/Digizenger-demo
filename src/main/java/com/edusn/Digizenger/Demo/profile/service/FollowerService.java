package com.edusn.Digizenger.Demo.profile.service;

import com.edusn.Digizenger.Demo.auth.dto.response.Response;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;

public interface FollowerService {

    ResponseEntity<Response> followToProfile(HttpServletRequest request, Long toFollowProfileId);

    ResponseEntity<Response> unFollowToProfile(HttpServletRequest request, Long toUnFollowProfileId);

    ResponseEntity<Response> getProfileFollowersByPage(int _page,
                                                       int _limit,
                                                       Long profileId,
                                                       HttpServletRequest request);
}
