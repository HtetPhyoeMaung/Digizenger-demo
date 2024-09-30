package com.edusn.Digizenger.Demo.profile.service.impl.profile_image;

import com.edusn.Digizenger.Demo.auth.dto.response.Response;
import com.edusn.Digizenger.Demo.auth.entity.User;
import com.edusn.Digizenger.Demo.exception.ProfileImageNotFoundException;
import com.edusn.Digizenger.Demo.profile.entity.Profile;
import com.edusn.Digizenger.Demo.profile.repo.ProfileRepository;
import com.edusn.Digizenger.Demo.profile.service.ProfileImageService;
import com.edusn.Digizenger.Demo.storage.StorageService;
import com.edusn.Digizenger.Demo.utilis.GetUserByRequest;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class ProfileImageServiceImpl implements ProfileImageService {

    private final ProfileRepository profileRepository;
    private final StorageService storageService;
    private final GetUserByRequest getUserByRequest;

    @Override
    public ResponseEntity<Response> uploadProfileImage(MultipartFile file, HttpServletRequest request) throws IOException {

        String profileImageName = storageService.uploadImage(file);
        User user = getUserByRequest.getUser(request);
        Profile profile = profileRepository.findByUser(user);
        profile.setProfileImageName(profileImageName);
        profileRepository.save(profile);

        Response response = Response.builder()
                .statusCode(HttpStatus.OK.value())
                .message("successfully uploaded profile image.")
                .build();

        return new  ResponseEntity<>(response, HttpStatus.OK);
    }


    @Override
    public ResponseEntity<Response> deleteProfileImage(HttpServletRequest request){

        User user = getUserByRequest.getUser(request);
        Profile profile = profileRepository.findByUser(user);
        if(profile.getProfileImageName() == null)
            throw new ProfileImageNotFoundException("Image cannot found by name : "+ profile.getProfileImageName());
        storageService.deleteImage(profile.getProfileImageName());
        profile.setProfileImageName(null);
        profileRepository.save(profile);

        Response response = Response.builder()
                .statusCode(HttpStatus.OK.value())
                .message("successfully deleted profile image")
                .build();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Response> updateProfileImage(MultipartFile file, HttpServletRequest request) throws IOException {

        User user = getUserByRequest.getUser(request);
        Profile profile = profileRepository.findByUser(user);
        if(profile.getProfileImageName() == null)
            throw new ProfileImageNotFoundException("can't update the cover because image is null");
        String updateFilename = storageService.updateImage(file, profile.getProfileImageName());
        profile.setProfileImageName(updateFilename);
        profileRepository.save(profile);

        Response response = Response.builder()
                .statusCode(HttpStatus.OK.value())
                .message("successfully updated profile image.")
                .build();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
