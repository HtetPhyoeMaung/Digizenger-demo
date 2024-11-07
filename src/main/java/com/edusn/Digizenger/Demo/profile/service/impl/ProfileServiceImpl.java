package com.edusn.Digizenger.Demo.profile.service.impl;

import com.edusn.Digizenger.Demo.auth.dto.response.Response;
import com.edusn.Digizenger.Demo.auth.entity.User;
import com.edusn.Digizenger.Demo.exception.CustomNotFoundException;
import com.edusn.Digizenger.Demo.post.dto.UserDto;
import com.edusn.Digizenger.Demo.profile.dto.response.myProfile.*;
import com.edusn.Digizenger.Demo.profile.entity.Profile;
import com.edusn.Digizenger.Demo.profile.repo.ProfileRepository;
import com.edusn.Digizenger.Demo.profile.service.OtherProfileService;
import com.edusn.Digizenger.Demo.profile.service.ProfileService;
import com.edusn.Digizenger.Demo.storage.StorageService;
import com.edusn.Digizenger.Demo.utilis.GetUserByRequest;
import com.edusn.Digizenger.Demo.utilis.MapperUtil;
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
    private final OtherProfileService otherProfileService;
    private final StorageService storageService;
    private final GetUserByRequest getUserByRequest;
    private final MapperUtil mapperUtil;

    /** Create profile **/
    @Override
    public void createUserProfile(User user) {

        /* Create profile object */
        Profile profile = new Profile();
        String randomString = UrlGenerator.generateRandomString();
        profile.setUsername(randomString + "RD");
        profile.setUser(user);
        profileRepository.save(profile);
    }

    /** Get Logged-in user's Profile **/
    @Override
    public ResponseEntity<Response> showUserProfile(HttpServletRequest request) throws IOException {
        User user = getUserByRequest.getUser(request);
        UserDto userDto=mapperUtil.convertToUserDto(user,true);
        /** Service Provided **/
        if(!user.getProfile().getServiceProvidedList().isEmpty()){
            List<ServiceProvidedDto> serviceProvidedDtoList = user.getProfile().getServiceProvidedList().stream().map(
                    mapperUtil::convertToServiceProvidedDto
            ).collect(Collectors.toList());
            userDto.getProfileDto().setServiceProvidedDtoList(serviceProvidedDtoList);
        }
        /** Service **/
        if(!user.getProfile().getFollowers().isEmpty()){
            userDto.getProfileDto().setFollowerCount((long) user.getProfile().getFollowers().size());
        }
        /** Following **/
        if(!user.getProfile().getFollowing().isEmpty()){
            userDto.getProfileDto().setFollowingCount((long) user.getProfile().getFollowing().size());
        }
        /** Neighbors **/
        if(!user.getProfile().getNeighbors().isEmpty()){
            userDto.getProfileDto().setNeighborCount((long) user.getProfile().getNeighbors().size());
        }
        /* Education Histories **/
        if(!user.getProfile().getEducationHistories().isEmpty()){
            List<EducationHistoryDto> educationHistoryDtoList = user.getProfile().getEducationHistories().stream().map(
                    mapperUtil::convertToEducationHistoryDto
            ).collect(Collectors.toList());
            userDto.getProfileDto().setEducationHistoryDtoList(educationHistoryDtoList);
        }
        /* Career Histories **/
        if(!user.getProfile().getCareerHistories().isEmpty()){
            List<CareerHistoryDto> careerHistoryDtoList = user.getProfile().getCareerHistories().stream().map(
                    mapperUtil::convertToCareerHistoryDto
            ).collect(Collectors.toList());
            userDto.getProfileDto().setCareerHistoryDtoList(careerHistoryDtoList);
        }
        Response response = Response.builder()
                .statusCode(HttpStatus.OK.value())
                .message("successfully showed existed profile data..")
                .userDto(userDto)
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Response> getProfileByProfileUrlLink(String username, HttpServletRequest request) throws IOException {

        User user = getUserByRequest.getUser(request);
        if(user.getProfile().getUsername().equals(username)){
            return showUserProfile(request);
        }
        else {
            Profile otherProfile = profileRepository.findByUsername(username)
                    .orElseThrow(()-> new CustomNotFoundException("profile not found by url which have username : "+username));
            return otherProfileService.showOtherUserProfile(otherProfile, user.getProfile());
        }
    }

    @Override
    public ResponseEntity<Response> getProfileById(HttpServletRequest request, Long id) throws IOException {
        User user = getUserByRequest.getUser(request);
        Profile toFindProfile = profileRepository.findById(id)
                .orElseThrow(() -> new CustomNotFoundException("profile is not exists by id : "+id));
        return otherProfileService.showOtherUserProfile(toFindProfile, user.getProfile());
    }

}
