package com.edusn.Digizenger.Demo.profile.service;

import com.edusn.Digizenger.Demo.auth.dto.response.Response;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;

public interface ProfilePostService {
    ResponseEntity<Response> getProfilePosts(HttpServletRequest request,
                                            int _page,
                                            int _limit);

    ResponseEntity<Response> getOtherProfilePosts(HttpServletRequest request,
                                                  String profileLinkUrl,
                                                  int _page,
                                                  int _limit);
}
