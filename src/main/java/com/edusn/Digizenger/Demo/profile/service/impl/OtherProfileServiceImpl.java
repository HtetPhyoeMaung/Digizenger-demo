package com.edusn.Digizenger.Demo.profile.service.impl;

import com.edusn.Digizenger.Demo.auth.dto.response.Response;
import com.edusn.Digizenger.Demo.auth.entity.User;
import com.edusn.Digizenger.Demo.post.dto.PostDto;
import com.edusn.Digizenger.Demo.profile.dto.response.otherProfile.OtherProfileDto;
import com.edusn.Digizenger.Demo.profile.dto.response.otherProfile.OtherUserProfileDto;
import com.edusn.Digizenger.Demo.profile.entity.Profile;
import com.edusn.Digizenger.Demo.profile.service.OtherProfileService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OtherProfileServiceImpl implements OtherProfileService {

    private final ModelMapper modelMapper;

    @Override
    public ResponseEntity<Response> showOtherUserProfile(Profile otherProfile) {
        /* to get other user */
        User otherUser = otherProfile.getUser();

        /* map OtherUserProfileDto and otherUser */
        OtherUserProfileDto otherUserProfileDto = modelMapper.map(otherUser, OtherUserProfileDto.class);

        /* To check posts whether exist or non-exit and map PostDto and otherUser's post */
        if(otherUser.getPosts() != null){
            List<PostDto> postDtoList = otherUser.getPosts().stream().map(
                    post -> modelMapper.map(post, PostDto.class)
            ).collect(Collectors.toList());
            otherUserProfileDto.setPostDtoList(postDtoList);
        }

        /* Map OtherProfileDto and otherProfile */
        OtherProfileDto otherProfileDto = modelMapper.map(otherProfile, OtherProfileDto.class);

        /* Now save otherUserProfileDto to otherProfileDto */
        otherProfileDto.setOtherUserProfileDto(otherUserProfileDto);

        Response response = Response.builder()
                .statusCode(HttpStatus.OK.value())
                .message("successfully got another user profile..")
                .otherProfileDto(otherProfileDto)
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
