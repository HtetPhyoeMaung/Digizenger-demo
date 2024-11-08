package com.edusn.Digizenger.Demo.profile.service.impl.cover_image;

import com.edusn.Digizenger.Demo.auth.dto.response.Response;
import com.edusn.Digizenger.Demo.auth.entity.User;
import com.edusn.Digizenger.Demo.exception.CustomNotFoundException;
import com.edusn.Digizenger.Demo.profile.entity.Profile;
import com.edusn.Digizenger.Demo.profile.repo.ProfileRepository;
import com.edusn.Digizenger.Demo.profile.service.CoverImageService;
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
public class CoverImageServiceImpl implements CoverImageService {

    private final ProfileRepository profileRepository;
    private final StorageService storageService;
    private final GetUserByRequest getUserByRequest;

    @Override
    public ResponseEntity<Response> uploadCoverImage(MultipartFile file, HttpServletRequest request) throws IOException {

        String fileName = storageService.uploadImage(file);

        User user = getUserByRequest.getUser(request);


        user.getProfile().setCoverImageName(fileName);
        Profile savedProfile = profileRepository.save(user.getProfile());
        String coverImageUrl = storageService.getImageByName(savedProfile.getCoverImageName());

        Response response = Response.builder()
                .statusCode(HttpStatus.OK.value())
                .message("successfully uploaded profile image.")
                .coverImageUrl(coverImageUrl)
                .build();

        return new  ResponseEntity<>(response, HttpStatus.OK);
    }


    @Override
    public ResponseEntity<Response> deleteCoverImage(HttpServletRequest request){

        User user = getUserByRequest.getUser(request);

        if(user.getProfile().getCoverImageName() == null) throw new CustomNotFoundException("Image cannot found by name!");

        storageService.deleteImage(user.getProfile().getCoverImageName());
        user.getProfile().setCoverImageName(null);

        profileRepository.save(user.getProfile());

        Response response = Response.builder()
                .statusCode(HttpStatus.OK.value())
                .message("successfully deleted cover image.")
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Response> updateCoverImage(MultipartFile file, HttpServletRequest request) throws IOException {

        User user = getUserByRequest.getUser(request);

//        Profile profile = profileRepository.findByUser(user);

        if(user.getProfile().getCoverImageName() == null) throw new CustomNotFoundException("can't found cover image ");

        String updateFilename = storageService.updateImage(file, user.getProfile().getCoverImageName());
        user.getProfile().setCoverImageName(updateFilename);
        Profile savedProfile = profileRepository.save(user.getProfile());
        String coverImageUrl = storageService.getImageByName(savedProfile.getCoverImageName());

        Response response = Response.builder()
                .statusCode(HttpStatus.OK.value())
                .message("successfully updated cover image.")
                .coverImageUrl(coverImageUrl)
                .build();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
