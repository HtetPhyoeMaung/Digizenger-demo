package com.edusn.Digizenger.Demo.profile.service.impl;

import com.edusn.Digizenger.Demo.auth.dto.response.Response;
import com.edusn.Digizenger.Demo.auth.entity.User;
import com.edusn.Digizenger.Demo.auth.repo.UserRepository;
import com.edusn.Digizenger.Demo.exception.ProfileNotFoundException;
import com.edusn.Digizenger.Demo.exception.UserNotFoundException;
import com.edusn.Digizenger.Demo.post.dto.PostDto;
import com.edusn.Digizenger.Demo.post.service.impl.PostServiceImpl;
import com.edusn.Digizenger.Demo.profile.dto.response.myProfile.ProfileDto;
import com.edusn.Digizenger.Demo.profile.dto.response.myProfile.UserForProfileDto;
import com.edusn.Digizenger.Demo.profile.entity.Profile;
import com.edusn.Digizenger.Demo.profile.repo.ProfileRepository;
import com.edusn.Digizenger.Demo.profile.service.OtherProfileService;
import com.edusn.Digizenger.Demo.profile.service.ProfileService;
import com.edusn.Digizenger.Demo.security.JWTService;
import com.edusn.Digizenger.Demo.storage.StorageService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService {

    private final ProfileRepository profileRepository;
    private final ModelMapper modelMapper;
    private final JWTService jwtService;
    private final UserRepository userRepository;
    private final OtherProfileService otherProfileService;
    private final StorageService storageService;

    /** Create profile **/
    @Override
    public void createUserProfile(User user) {

        /* Create profile object */
        Profile profile = new Profile();

        String randomString = UrlGenerator.generateRandomString();
        profile.setProfileLinkUrl("http://localhost:8080/api/v1/profile/"+randomString);
        profile.setUser(user);
        profileRepository.save(profile);
    }


    /** Get Logged-in user's Profile **/
    @Override
    public ResponseEntity<Response> showUserProfile(HttpServletRequest request) throws IOException {

        /* Get Token form Header */
        String token = jwtService.getJWTFromRequest(request);
        /* Get Email from token */
        String email = jwtService.extractUsername(token);
        /* Get user find by email */
        User user = userRepository.findByEmail(email).orElseThrow(() ->
                new UserNotFoundException("user cannot found by email : " + email));

        Profile profile = profileRepository.findByUser(user);


        if(profile.getUsername() != null){
            profile.setProfileLinkUrl("http://localhost:8080/api/v1/profile/"+profile.getUsername());
            profileRepository.save(profile);
        }

        ProfileDto existProfileDto = modelMapper.map(profile, ProfileDto.class);
        /* Map the userProfileDto and user from existed profile */
        UserForProfileDto userForProfileDto = modelMapper.map(profile.getUser(), UserForProfileDto.class);

        /* To check user's posts whether exist or non-exist */
        if(profile.getUser().getPosts() != null){

            /* If posts exist,map postDtoList and user's post ,and then save to the userProfileDto */
            List<PostDto> postDtoList = profile.getUser().getPosts().stream().map(
                    post -> PostServiceImpl.convertToPostDto(post)
            ).collect(Collectors.toList());

            userForProfileDto.setPostDtoList(postDtoList);
        }
        /* also save userProfileDto to existProfileDto */
        existProfileDto.setUserForProfileDto(userForProfileDto);

        if(existProfileDto.getProfileImageName() != null){
            existProfileDto.setProfileImageByte(
                    storageService.getImageByName(existProfileDto.getProfileImageName())
            );
        }

        if(existProfileDto.getCoverImageName() != null){
            existProfileDto.setCoverImageByte(
                    storageService.getImageByName(existProfileDto.getCoverImageName())
            );
        }

        Response response = Response.builder()
                .statusCode(HttpStatus.OK.value())
                .message("successfully showed existed profile data..")
                .profileDto(existProfileDto)
                .build();
        /* Now return existed user's profile to client */
        return new ResponseEntity<>(response, HttpStatus.OK);
    }



    /** Get Logged-in user's profile or other user's profile by profile url **/
    @Override
    public ResponseEntity<Response> getProfileByProfileUrlLink(String profileUrl, HttpServletRequest request) throws IOException {

        /* Get Token form Header */
        String token = jwtService.getJWTFromRequest(request);
        /* Get Email from token */
        String email = jwtService.extractUsername(token);
        /* Get user find by email */
        User user = userRepository.findByEmail(email).orElseThrow(() ->
                new UserNotFoundException("user cannot found by email : " + email));

        Profile profile = profileRepository.findByUser(user);

        if(profile == null){ throw new ProfileNotFoundException("profile cannot found by user : " + user.toString());}

        /* To check if profile.getProfileLinkUrl() == myProfileLink show my profile data */
        /* Or else show other user's profile data */
        if(profile.getProfileLinkUrl() == "http://localhost:8080/api/v1/profile/"+profileUrl){
            return showUserProfile(request);
        }
        else {
            Profile otherProfile = profileRepository.findByProfileLinkUrl("http://localhost:8080/api/v1/profile/"+profileUrl);
            if(otherProfile == null){throw new ProfileNotFoundException("profile cannot found by url : "+profile.getProfileLinkUrl());}
            return otherProfileService.showOtherUserProfile(otherProfile);
        }
    }
}
