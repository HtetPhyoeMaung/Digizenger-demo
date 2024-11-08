package com.edusn.Digizenger.Demo.profile.service.impl.profile_career;

import com.edusn.Digizenger.Demo.auth.dto.response.Response;
import com.edusn.Digizenger.Demo.auth.entity.User;
import com.edusn.Digizenger.Demo.exception.CustomNotFoundException;
import com.edusn.Digizenger.Demo.profile.entity.Profile;
import com.edusn.Digizenger.Demo.profile.repo.ProfileRepository;
import com.edusn.Digizenger.Demo.profile.service.ProfileCareerService;
import com.edusn.Digizenger.Demo.utilis.GetUserByRequest;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProfileCareerServiceImpl implements ProfileCareerService {

    private final GetUserByRequest getUserByRequest;
    private final ProfileRepository profileRepository;

    @Override
    public ResponseEntity<Response> uploadCareer(String careerName, HttpServletRequest request) {

        User user = getUserByRequest.getUser(request);
        user.getProfile().setProfileCareer(careerName);
        profileRepository.save(user.getProfile());

        Response response = Response.builder()
                .statusCode(HttpStatus.OK.value())
                .message("successfully uploaded profile career name.")
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Response> removeCareer(HttpServletRequest request) {

        User user = getUserByRequest.getUser(request);
        if(user.getProfile().getProfileCareer() == null)
            throw new CustomNotFoundException("career is not exist.");
        user.getProfile().setProfileCareer(null);
        profileRepository.save(user.getProfile());

        Response response = Response.builder()
                .statusCode(HttpStatus.OK.value())
                .message("successfully removed profile career.")
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
