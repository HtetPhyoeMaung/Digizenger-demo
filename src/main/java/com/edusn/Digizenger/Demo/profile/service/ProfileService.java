package com.edusn.Digizenger.Demo.profile.service;

import com.edusn.Digizenger.Demo.auth.dto.response.Response;
import com.edusn.Digizenger.Demo.auth.entity.User;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public interface ProfileService {

    /* create user profiles */
    void createUserProfile(HttpServletRequest request);

    /* To see the user's profile data */
    ResponseEntity<Response> showUserProfile(HttpServletRequest request);

    /* To get the profile by profile url link */
    ResponseEntity<Response> getProfileByProfileUrlLink(String profileUrl, HttpServletRequest request);
}
