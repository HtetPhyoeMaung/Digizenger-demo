package com.edusn.Digizenger.Demo.profile.service.impl.username;

import com.edusn.Digizenger.Demo.auth.dto.response.Response;
import com.edusn.Digizenger.Demo.auth.entity.User;
import com.edusn.Digizenger.Demo.profile.entity.Profile;
import com.edusn.Digizenger.Demo.profile.repo.ProfileRepository;
import com.edusn.Digizenger.Demo.profile.service.UsernameService;
import com.edusn.Digizenger.Demo.profile.service.impl.UrlGenerator;
import com.edusn.Digizenger.Demo.utilis.GetUserByRequest;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UsernameServiceImpl implements UsernameService {

    private final GetUserByRequest getUserByRequest;
    private final ProfileRepository profileRepository;
    @Value("${app.profileUrl}")
    private String profileUrl;

    @Override
    public ResponseEntity<Response> uploadUsername(String username, HttpServletRequest request) {

        User user = getUserByRequest.getUser(request);
        Profile profile = profileRepository.findByUser(user);
        profile.setUsername(username.toLowerCase().trim());
        profileRepository.save(profile);

        Response response = Response.builder()
                .statusCode(HttpStatus.OK.value())
                .message("successfully uploaded username")
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Response> removeUsername(HttpServletRequest request) {

        User user = getUserByRequest.getUser(request);
        Profile profile = profileRepository.findByUser(user);
        if(profile.getUsername() == null)
            throw new UsernameNotFoundException("username not found by : "+ profile.getUsername());
        profile.setUsername(null);
        String randomString = UrlGenerator.generateRandomString();
        profile.setProfileLinkUrl(profileUrl+randomString);
        profileRepository.save(profile);

        Response response = Response.builder()
                .statusCode(HttpStatus.OK.value())
                .message("successfully removed username.")
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
