package com.edusn.Digizenger.Demo.profile.controller;

import com.edusn.Digizenger.Demo.auth.dto.response.Response;
import com.edusn.Digizenger.Demo.profile.service.*;
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
    private final BioProfileService bioProfileService;
    private final UsernameService usernameService;
    private final ProfileCareerService profileCareerService;

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

    @PostMapping("/p-image")
    public ResponseEntity<Response> uploadProfileImage(@RequestParam("file") MultipartFile file, HttpServletRequest request) throws IOException {
        return profileImageService.uploadProfileImage(file, request);
    }

    @DeleteMapping("/p-image")
    public ResponseEntity<Response> deleteProfileImage(HttpServletRequest request) throws FileNotFoundException {
        return profileImageService.deleteProfileImage(request);
    }

    @PutMapping("/p-image")
    public ResponseEntity<Response> updateProfileImage(@RequestParam("file") MultipartFile file, HttpServletRequest request) throws IOException {
        return  profileImageService.updateProfileImage(file, request);
    }

    /** Cover Image **/

    @PostMapping("/c-image")
    public ResponseEntity<Response> uploadCoverImage(@RequestParam("file") MultipartFile file, HttpServletRequest request) throws IOException {
        return coverImageService.uploadCoverImage(file, request);
    }

    @DeleteMapping("/c-image")
    public ResponseEntity<Response> deleteCoverImage(HttpServletRequest request) throws FileNotFoundException {
        return coverImageService.deleteCoverImage(request);
    }

    @PutMapping("/c-image")
    public ResponseEntity<Response> updateCoverImage(@RequestParam("file") MultipartFile file, HttpServletRequest request) throws IOException {
        return coverImageService.updateCoverImage(file, request);
    }

    /** Bio **/

    @PostMapping("/bio")
    public ResponseEntity<Response> uploadBio(@RequestParam("bio") String bio, HttpServletRequest request){
        return bioProfileService.uploadBio(bio, request);
    }

    @DeleteMapping("/bio")
    public ResponseEntity<Response> removeBio(HttpServletRequest request){
        return bioProfileService.removeBio(request);
    }

    /** Username **/

    @PostMapping("/username")
    public ResponseEntity<Response> uploadUsername(@RequestParam("username") String username, HttpServletRequest request){
        return usernameService.uploadUsername(username, request);
    }

    @DeleteMapping("/username")
    public ResponseEntity<Response> removeUsername(HttpServletRequest request){
        return usernameService.removeUsername(request);
    }

    /** Profile Career **/

    @PostMapping("/career")
    public ResponseEntity<Response> uploadCareer(@RequestParam("profileCareer") String profileCareer, HttpServletRequest request){
        return profileCareerService.uploadCareer(profileCareer, request);
    }

    @DeleteMapping("/career")
    public ResponseEntity<Response> removeCareer(HttpServletRequest request){
        return profileCareerService.removeCareer(request);
    }



}
