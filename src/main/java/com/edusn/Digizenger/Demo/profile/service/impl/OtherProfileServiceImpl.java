package com.edusn.Digizenger.Demo.profile.service.impl;

import com.edusn.Digizenger.Demo.auth.dto.response.Response;
import com.edusn.Digizenger.Demo.auth.entity.User;
import com.edusn.Digizenger.Demo.post.dto.PostDto;
import com.edusn.Digizenger.Demo.profile.dto.response.myProfile.CareerHistoryDto;
import com.edusn.Digizenger.Demo.profile.dto.response.otherProfile.OtherProfileDto;
import com.edusn.Digizenger.Demo.profile.dto.response.otherProfile.OtherUserForProfileDto;
import com.edusn.Digizenger.Demo.profile.entity.CareerHistory;
import com.edusn.Digizenger.Demo.profile.entity.Profile;
import com.edusn.Digizenger.Demo.profile.service.OtherProfileService;
import com.edusn.Digizenger.Demo.storage.StorageService;
import com.edusn.Digizenger.Demo.utilis.UrlConverter;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


import java.net.MalformedURLException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OtherProfileServiceImpl implements OtherProfileService {

    private final ModelMapper modelMapper;
    private final StorageService storageService;

    @Override
    public ResponseEntity<Response> showOtherUserProfile(Profile otherProfile)  {

        User otherUser = otherProfile.getUser();
        OtherUserForProfileDto otherUserForProfileDto = modelMapper.map(otherUser, OtherUserForProfileDto.class);
        if(otherUser.getPosts() != null){
            List<PostDto> postDtoList = otherUser.getPosts().stream().map(
                    post -> modelMapper.map(post, PostDto.class)
            ).collect(Collectors.toList());
            otherUserForProfileDto.setPostDtoList(postDtoList);
        }
        OtherProfileDto otherProfileDto = modelMapper.map(otherProfile, OtherProfileDto.class);
        otherProfileDto.setOtherUserForProfileDto(otherUserForProfileDto);

        if(otherProfileDto.getProfileImageName() != null){
            otherProfileDto.setProfileImageUrl(
                    storageService.getImageByName(otherProfileDto.getProfileImageName())
            );
        }
        if(otherProfileDto.getCoverImageName() != null){
            otherProfileDto.setCoverImageUrl(
                    storageService.getImageByName(otherProfileDto.getCoverImageName())
            );
        }

        /** For CareerHistory **/

        if(!otherProfile.getCareerHistoryList().isEmpty()){
            List<CareerHistoryDto> careerHistoryDtoList = otherProfile.getCareerHistoryList().stream().map(
                    careerHistory -> {
                        CareerHistoryDto careerHistoryDto = modelMapper.map(careerHistory, CareerHistoryDto.class);
                        try {
                            careerHistoryDto.setCompanyLogoUrl(UrlConverter.convertToUrl(careerHistory.getCompanyLogoUrl()));
                        } catch (MalformedURLException e) {
                            throw new RuntimeException(e);
                        }
                        return careerHistoryDto;
                    }).collect(Collectors.toList());
            otherProfileDto.setCareerHistoryDtoList(careerHistoryDtoList);
        }

        Response response = Response.builder()
                .statusCode(HttpStatus.OK.value())
                .message("successfully got another user profile..")
                .otherProfileDto(otherProfileDto)
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
