package com.edusn.Digizenger.Demo.profile.service;

import com.edusn.Digizenger.Demo.auth.dto.response.Response;
import com.edusn.Digizenger.Demo.auth.entity.User;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.IOException;

public interface ProfileService {

    /* create user profiles */
    void createUserProfile(User user);

    /* To see the user's profile data */
    ResponseEntity<Response> showUserProfile(HttpServletRequest request) throws IOException;

    /* To get the profile by profile url link */
    ResponseEntity<Response> getProfileByProfileUrlLink(String profileUrl, HttpServletRequest request) throws IOException;
}
