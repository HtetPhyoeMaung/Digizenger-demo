package com.edusn.Digizenger.Demo.profile.service.impl.bio;

import com.edusn.Digizenger.Demo.auth.dto.response.Response;
import com.edusn.Digizenger.Demo.auth.entity.User;
import com.edusn.Digizenger.Demo.exception.BioNotFoundException;
import com.edusn.Digizenger.Demo.profile.entity.Profile;
import com.edusn.Digizenger.Demo.profile.repo.ProfileRepository;
import com.edusn.Digizenger.Demo.profile.service.BioProfileService;
import com.edusn.Digizenger.Demo.utilis.GetUserByRequest;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BioProfileServiceImpl implements BioProfileService {

    private final ProfileRepository profileRepository;
    private final GetUserByRequest getUserByRequest;

    @Override
    public ResponseEntity<Response> uploadBio(String bio, HttpServletRequest request) {

        User user = getUserByRequest.getUser(request);
        Profile profile = profileRepository.findByUser(user);
        profile.setBio(bio);
        profileRepository.save(profile);

        Response response = Response.builder()
                .statusCode(HttpStatus.OK.value())
                .message("successfully uploaded bio.")
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Response> removeBio(HttpServletRequest request) {

        User user = getUserByRequest.getUser(request);
        Profile profile = profileRepository.findByUser(user);

        if(profile.getBio() == null) throw new BioNotFoundException("Bio not found in profile.");

        profile.setBio(null);
        profileRepository.save(profile);

        Response response = Response.builder()
                .statusCode(HttpStatus.OK.value())
                .message("successfully removed bio.")
                .build();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
