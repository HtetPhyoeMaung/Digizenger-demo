package com.edusn.Digizenger.Demo.profile.service.impl;

import com.edusn.Digizenger.Demo.auth.dto.response.Response;
import com.edusn.Digizenger.Demo.auth.entity.User;
import com.edusn.Digizenger.Demo.post.dto.PostDto;
import com.edusn.Digizenger.Demo.post.service.PostService;
import com.edusn.Digizenger.Demo.post.service.impl.PostServiceImpl;
import com.edusn.Digizenger.Demo.profile.dto.response.ProfileDto;
import com.edusn.Digizenger.Demo.profile.dto.response.UserProfileDto;
import com.edusn.Digizenger.Demo.profile.entity.Profile;
import com.edusn.Digizenger.Demo.profile.repo.ProfileRepository;
import com.edusn.Digizenger.Demo.profile.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService {

    private final ProfileRepository profileRepository;
    private final ModelMapper modelMapper;


    @Override
    public ResponseEntity<Response> showUserProfile(User user) {

        Profile profile = profileRepository.findByUser(user);

        if(profile == null){
            Profile newProfile = new Profile();
            newProfile.setUser(user);
            Profile updateProfile = profileRepository.save(newProfile);

            UserProfileDto userProfileDto = modelMapper.map(user, UserProfileDto.class);
            ProfileDto profileDto = modelMapper.map(updateProfile, ProfileDto.class);

            profileDto.setUserProfileDto(userProfileDto);

            Response response = Response.builder()
                    .statusCode(HttpStatus.OK.value())
                    .message("successfully showed created profile data.")
                    .profileDto(profileDto)
                    .build();
            return new ResponseEntity<>(response, HttpStatus.OK);
        }

        ProfileDto existProfileDto = modelMapper.map(profile, ProfileDto.class);

        if(profile.getUser() != null){
            UserProfileDto userProfileDto = modelMapper.map(profile.getUser(), UserProfileDto.class);

            if(profile.getUser().getPosts() != null){
                List<PostDto> postDtoList = profile.getUser().getPosts().stream().map(
                        post -> PostServiceImpl.convertToPostDto(post)
                ).collect(Collectors.toList());

                userProfileDto.setPostDtoList(postDtoList);
            }
            existProfileDto.setUserProfileDto(userProfileDto);
        }


        Response response = Response.builder()
                .statusCode(HttpStatus.OK.value())
                .message("successfully showed existed profile data..")
                .profileDto(existProfileDto)
                .build();

        return new ResponseEntity<>(response, HttpStatus.OK);

    }





}
