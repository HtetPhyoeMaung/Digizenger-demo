package com.edusn.Digizenger.Demo.profile.service.impl;

import com.edusn.Digizenger.Demo.auth.dto.response.Response;
import com.edusn.Digizenger.Demo.auth.entity.User;
import com.edusn.Digizenger.Demo.auth.repo.UserRepository;
import com.edusn.Digizenger.Demo.exception.CoverImageNotFoundException;
import com.edusn.Digizenger.Demo.exception.UserNotFoundException;
import com.edusn.Digizenger.Demo.profile.entity.Profile;
import com.edusn.Digizenger.Demo.profile.repo.ProfileRepository;
import com.edusn.Digizenger.Demo.profile.service.CoverImageService;
import com.edusn.Digizenger.Demo.security.JWTService;
import com.edusn.Digizenger.Demo.storage.StorageService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;

@Service
@RequiredArgsConstructor
public class CoverImageServiceImpl implements CoverImageService {

    private final ProfileRepository profileRepository;
    private final JWTService jwtService;
    private final UserRepository userRepository;
    private final StorageService storageService;

    @Override
    public ResponseEntity<Response> uploadCoverImage(MultipartFile file, HttpServletRequest request) throws IOException {

        String fileName = storageService.uploadImage(file);

        /* Get Token form Header */
        String token = jwtService.getJWTFromRequest(request);

        /* Get Email from token */
        String email = jwtService.extractUsername(token);

        /* Get user find by email */
        User user = userRepository.findByEmail(email).orElseThrow(() ->
                new UserNotFoundException("user cannot found by email : " + email));

        Profile profile = profileRepository.findByUser(user);

            profile.setCoverImageName(fileName);
            profileRepository.save(profile);

        Response response = Response.builder()
                .statusCode(HttpStatus.OK.value())
                .message("successfully uploaded profile image.")
                .build();

        return new  ResponseEntity<>(response, HttpStatus.OK);
    }


    @Override
    public ResponseEntity<Response> deleteCoverImage(HttpServletRequest request){

        /* Get Token form Header */
        String token = jwtService.getJWTFromRequest(request);
        /* Get Email from token */
        String email = jwtService.extractUsername(token);
        /* Get user find by email */
        User user = userRepository.findByEmail(email).orElseThrow(() ->
                new UserNotFoundException("user cannot found by email : " + email));
        Profile profile = profileRepository.findByUser(user);

        if(profile.getCoverImageName() == null) throw new CoverImageNotFoundException("Image cannot found by name : "+ profile.getCoverImageName());

        storageService.deleteImage(profile.getCoverImageName());
        profile.setCoverImageName(null);

        profileRepository.save(profile);

        Response response = Response.builder()
                .statusCode(HttpStatus.OK.value())
                .message("successfully deleted cover image.")
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Response> updateCoverImage(MultipartFile file, HttpServletRequest request) throws IOException {
        /* Get Token form Header */
        String token = jwtService.getJWTFromRequest(request);
        /* Get Email from token */
        String email = jwtService.extractUsername(token);
        /* Get user find by email */
        User user = userRepository.findByEmail(email).orElseThrow(() ->
                new UserNotFoundException("user cannot found by email : " + email));
        Profile profile = profileRepository.findByUser(user);

        if(profile.getCoverImageName() == null) throw new CoverImageNotFoundException("can't found cover image ");

        String updateFilename = storageService.updateImage(file, profile.getCoverImageName());
        profile.setCoverImageName(updateFilename);

        Response response = Response.builder()
                .statusCode(HttpStatus.OK.value())
                .message("successfully updated cover image.")
                .build();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
