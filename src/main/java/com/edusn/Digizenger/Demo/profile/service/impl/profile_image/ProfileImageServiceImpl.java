package com.edusn.Digizenger.Demo.profile.service.impl.profile_image;

import com.edusn.Digizenger.Demo.auth.dto.response.Response;
import com.edusn.Digizenger.Demo.auth.entity.User;
import com.edusn.Digizenger.Demo.exception.CustomNotFoundException;
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
        user.getProfile().setProfileImageName(profileImageName);
         profileRepository.save(user.getProfile());
        Response response = Response.builder()
                .statusCode(HttpStatus.CREATED.value())
                .message("successfully uploaded profile image.")
                .profileImageUrl(storageService.getImageByName(profileImageName))
                .build();

        return new  ResponseEntity<>(response, HttpStatus.CREATED);
    }


    @Override
    public ResponseEntity<Response> deleteProfileImage(HttpServletRequest request){

        User user = getUserByRequest.getUser(request);
        if(user.getProfile().getProfileImageName() == null)
            throw new CustomNotFoundException("Image cannot found by name");
        storageService.deleteImage(user.getProfile().getProfileImageName());
        user.getProfile().setProfileImageName(null);
        profileRepository.save(user.getProfile());

        Response response = Response.builder()
                .statusCode(HttpStatus.NO_CONTENT.value())
                .message("successfully deleted profile image")
                .build();

        return new ResponseEntity<>(response, HttpStatus.NO_CONTENT);
    }

    @Override
    public ResponseEntity<Response> updateProfileImage(MultipartFile file, HttpServletRequest request) throws IOException {

        User user = getUserByRequest.getUser(request);
        if(user.getProfile().getProfileImageName() == null)
            throw new CustomNotFoundException("can't update the cover because image is null");
        String updateFilename = storageService.updateImage(file, user.getProfile().getProfileImageName());
        user.getProfile().setProfileImageName(updateFilename);
        profileRepository.save(user.getProfile());

        Response response = Response.builder()
                .statusCode(HttpStatus.OK.value())
                .message("successfully updated profile image.")
                .profileImageUrl(storageService.getImageByName(updateFilename))
                .build();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
