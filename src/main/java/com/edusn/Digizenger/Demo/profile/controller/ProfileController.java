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

import java.io.FileNotFoundException;
import java.io.IOException;

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
    public ResponseEntity<Response> getProfile(HttpServletRequest request) throws IOException {
        return profileService.showUserProfile(request);
    }


    @GetMapping("/{username}")
    public ResponseEntity<Response> getProfileByUrl(@PathVariable("username") String username,
                                                    HttpServletRequest request) throws IOException {
        return profileService.getProfileByProfileUrlLink(username,request);
    }

    /** Profile Image **/

    @PostMapping("/p-image/upload")
    public ResponseEntity<Response> uploadProfileImage(@RequestPart("file") MultipartFile file, HttpServletRequest request) throws IOException {
        return profileImageService.uploadProfileImage(file, request);
    }

    @DeleteMapping("/p-image/delete")
    public ResponseEntity<Response> deleteProfileImage(HttpServletRequest request) throws FileNotFoundException {
        return profileImageService.deleteProfileImage(request);
    }

    @PutMapping("/p-image/update")
    public ResponseEntity<Response> updateProfileImage(@RequestPart("file") MultipartFile file, HttpServletRequest request) throws IOException {
        return  profileImageService.updateProfileImage(file, request);
    }

    /** Cover Image **/

    @PostMapping("/c-image/upload")
    public ResponseEntity<Response> uploadCoverImage(@RequestPart("file") MultipartFile file, HttpServletRequest request) throws IOException {
        return coverImageService.uploadCoverImage(file, request);
    }

    @DeleteMapping("/c-image/delete")
    public ResponseEntity<Response> deleteCoverImage(HttpServletRequest request) throws FileNotFoundException {
        return coverImageService.deleteCoverImage(request);
    }

    @PutMapping("/c-image/update")
    public ResponseEntity<Response> updateCoverImage(@RequestPart("file") MultipartFile file, HttpServletRequest request) throws IOException {
        return coverImageService.updateCoverImage(file, request);
    }


}
