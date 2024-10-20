package com.edusn.Digizenger.Demo.profile.service;

import com.edusn.Digizenger.Demo.auth.dto.response.Response;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;

public interface ImagesService {

    //For my profile 9 image view
    ResponseEntity<Response> getProfileImages(HttpServletRequest request);

    //For my profile's images
    ResponseEntity<Response> getAllImages(HttpServletRequest request,int _page, int _limit);

    //For other Profile 9 image view
    ResponseEntity<Response> getOtherProfileImages(HttpServletRequest request, Long profileId);

    //For other Profile's images
    ResponseEntity<Response> getOtherAllImages(HttpServletRequest request,Long profileId, int _page, int _limit);
}
