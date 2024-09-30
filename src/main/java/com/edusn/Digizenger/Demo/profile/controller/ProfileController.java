package com.edusn.Digizenger.Demo.profile.controller;

import com.edusn.Digizenger.Demo.auth.dto.response.Response;
import com.edusn.Digizenger.Demo.profile.service.CoverImageService;
import com.edusn.Digizenger.Demo.profile.service.ProfileImageService;
import com.edusn.Digizenger.Demo.profile.service.ProfileService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/profile/")
public class ProfileController {

    private final ProfileService profileService;
    private final ProfileImageService profileImageService;
    private final CoverImageService coverImageService;

    @GetMapping("/test")
    public String test(){
        return "successfully test";
    }

    @GetMapping("/")
    public ResponseEntity<Response> getProfile(HttpServletRequest request){
        return profileService.showUserProfile(request);
    }


    @GetMapping("/{username}")
    public ResponseEntity<Response> getProfileByUrl(@PathVariable("username") String username,
                                                    HttpServletRequest request){
        return profileService.getProfileByProfileUrlLink(username,request);
    }

    @PostMapping("/p-image/upload")
    public ResponseEntity<Response> uploadProfileImage(@RequestPart("file") MultipartFile file, HttpServletRequest request){
        return profileImageService.uploadProfileImage(file, request);
    }

    @PostMapping("/c-image/upload")
    public ResponseEntity<Response> uploadCoverImage(@RequestPart("file") MultipartFile file, HttpServletRequest request){
        return coverImageService.uploadCoverImage(file, request);
    }


}
