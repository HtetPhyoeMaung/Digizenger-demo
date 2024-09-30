package com.edusn.Digizenger.Demo.profile.service;

import com.edusn.Digizenger.Demo.auth.dto.response.Response;
import com.edusn.Digizenger.Demo.profile.entity.Profile;
import org.springframework.http.ResponseEntity;

import java.io.IOException;

public interface OtherProfileService {
    /* To get other user's profile */
    ResponseEntity<Response> showOtherUserProfile(Profile OtherProfile) throws IOException;
}
