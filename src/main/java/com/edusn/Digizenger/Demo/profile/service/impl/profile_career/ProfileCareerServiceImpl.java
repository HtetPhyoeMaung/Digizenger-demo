package com.edusn.Digizenger.Demo.profile.service.impl.profile_career;

import com.edusn.Digizenger.Demo.auth.dto.response.Response;
import com.edusn.Digizenger.Demo.auth.entity.User;
import com.edusn.Digizenger.Demo.exception.CareerNotFoundException;
import com.edusn.Digizenger.Demo.profile.entity.Profile;
import com.edusn.Digizenger.Demo.profile.repo.ProfileRepository;
import com.edusn.Digizenger.Demo.profile.service.ProfileCareerService;
import com.edusn.Digizenger.Demo.profile.service.ProfileService;
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
        Profile profile = profileRepository.findByUser(user);
        profile.setProfileCareer(careerName);
        profileRepository.save(profile);

        Response response = Response.builder()
                .statusCode(HttpStatus.OK.value())
                .message("successfully uploaded profile career name.")
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Response> removeCareer(HttpServletRequest request) {

        User user = getUserByRequest.getUser(request);
        Profile profile = profileRepository.findByUser(user);
        if(profile.getProfileCareer() == null)
            throw new CareerNotFoundException("career is not exist.");
        profile.setProfileCareer(null);
        profileRepository.save(profile);

        Response response = Response.builder()
                .statusCode(HttpStatus.OK.value())
                .message("successfully removed profile career.")
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
