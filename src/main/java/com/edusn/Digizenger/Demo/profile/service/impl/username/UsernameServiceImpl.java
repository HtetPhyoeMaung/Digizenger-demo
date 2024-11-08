package com.edusn.Digizenger.Demo.profile.service.impl.username;

import com.amazonaws.services.kms.model.AlreadyExistsException;
import com.edusn.Digizenger.Demo.auth.dto.response.Response;
import com.edusn.Digizenger.Demo.auth.entity.User;
import com.edusn.Digizenger.Demo.exception.CustomNotFoundException;
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


    @Override
    public ResponseEntity<Response> uploadUsername(String username, HttpServletRequest request) {

        User user = getUserByRequest.getUser(request);
        if(profileRepository.existsByUsername(username))
            throw new CustomNotFoundException("username is already exists.Please enter another name.");
        user.getProfile().setUsername(username.trim());
        profileRepository.save( user.getProfile());

        Response response = Response.builder()
                .statusCode(HttpStatus.OK.value())
                .message("successfully uploaded username")
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Response> removeUsername(HttpServletRequest request) {

        User user = getUserByRequest.getUser(request);
        if( user.getProfile().getUsername() == null)
            throw new UsernameNotFoundException("username not found!" );
        String randomString = UrlGenerator.generateRandomString();
        user.getProfile().setUsername(randomString + "RD");
        profileRepository.save( user.getProfile());

        Response response = Response.builder()
                .statusCode(HttpStatus.OK.value())
                .message("successfully removed username.")
                .username(randomString)
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
