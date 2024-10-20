package com.edusn.Digizenger.Demo.profile.service.impl;

import com.edusn.Digizenger.Demo.auth.dto.response.Response;
import com.edusn.Digizenger.Demo.auth.entity.User;
import com.edusn.Digizenger.Demo.exception.ProfileNotFoundException;
import com.edusn.Digizenger.Demo.post.repo.LikeRepository;
import com.edusn.Digizenger.Demo.post.repo.PostRepository;
import com.edusn.Digizenger.Demo.post.repo.ViewRepository;
import com.edusn.Digizenger.Demo.profile.dto.response.myProfile.*;
import com.edusn.Digizenger.Demo.profile.entity.Profile;
import com.edusn.Digizenger.Demo.profile.repo.ProfileRepository;
import com.edusn.Digizenger.Demo.profile.service.OtherProfileService;
import com.edusn.Digizenger.Demo.profile.service.ProfileService;
import com.edusn.Digizenger.Demo.storage.StorageService;
import com.edusn.Digizenger.Demo.utilis.GetUserByRequest;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
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
    private final ViewRepository viewRepository;
    private final LikeRepository likeRepository;
    private final PostRepository postRepository;
    @Value("${app.profileUrl}")
    private String baseProfileUrl;

    /** Create profile **/
    @Override
    public void createUserProfile(User user) {

        /* Create profile object */
        Profile profile = new Profile();
        String randomString = UrlGenerator.generateRandomString();
        profile.setProfileLinkUrl(baseProfileUrl+randomString);
        profile.setUser(user);
        profileRepository.save(profile);
    }

    /** Get Logged-in user's Profile **/
    @Override
    public ResponseEntity<Response> showUserProfile(HttpServletRequest request) throws IOException {

        User user = getUserByRequest.getUser(request);
        Profile profile = user.getProfile();

        if(profile.getUsername() != null){
            profile.setProfileLinkUrl(baseProfileUrl+profile.getUsername());
            profileRepository.save(profile);
        }

        ProfileDto existProfileDto = modelMapper.map(profile, ProfileDto.class);
        UserForProfileDto userForProfileDto = modelMapper.map(profile.getUser(), UserForProfileDto.class);


        existProfileDto.setUserForProfileDto(userForProfileDto);
        if(existProfileDto.getProfileImageName() != null){
            existProfileDto.setProfileImageUrl(
                    storageService.getImageByName(existProfileDto.getProfileImageName())
            );
        }
        if(existProfileDto.getCoverImageName() != null){
            existProfileDto.setCoverImageUrl(
                    storageService.getImageByName(existProfileDto.getCoverImageName())
            );
        }


        /** Service Provided **/
        if(!profile.getServiceProvidedList().isEmpty()){
            List<ServiceProvidedDto> serviceProvidedDtoList = profile.getServiceProvidedList().stream().map(
                    serviceProvided -> modelMapper.map(serviceProvided, ServiceProvidedDto.class)
            ).collect(Collectors.toList());
            existProfileDto.setServiceProvidedDtoList(serviceProvidedDtoList);
        }

        /** Service **/
        if(!profile.getFollowers().isEmpty()){
            existProfileDto.setFollowersCount(Long.valueOf(profile.getFollowers().size()));
        }

        /** Following **/
        if(!profile.getFollowing().isEmpty()){
            existProfileDto.setFollowingCount(Long.valueOf(profile.getFollowing().size()));
        }

        /** Neighbors **/
        if(!profile.getNeighbors().isEmpty()){
            existProfileDto.setNeighborCount(Long.valueOf(profile.getNeighbors().size()));
        }

        /* Education Histories **/
        if(!profile.getEducationHistories().isEmpty()){
            List<EducationHistoryDto> educationHistoryDtoList = profile.getEducationHistories().stream().map(
                    educationHistory -> {
                        EducationHistoryDto educationHistoryDto = modelMapper.map(educationHistory, EducationHistoryDto.class);

                        SchoolDto schoolDto = modelMapper.map(educationHistory.getSchool(), SchoolDto.class);
                        if(educationHistory.getSchool().getLogoImageName() != null){
                            schoolDto.setLogoImageUrl(storageService.getImageByName(educationHistory.getSchool().getLogoImageName()));
                        }
                        educationHistoryDto.setSchoolDto(schoolDto);
                        return educationHistoryDto;

                    }
            ).collect(Collectors.toList());

            existProfileDto.setEducationHistoryDtoList(educationHistoryDtoList);
        }

        /* Career Histories **/
        if(!profile.getCareerHistories().isEmpty()){
            List<CareerHistoryDto> careerHistoryDtoList = profile.getCareerHistories().stream().map(
                    careerHistory -> {
                        CareerHistoryDto careerHistoryDto = modelMapper.map(careerHistory, CareerHistoryDto.class);

                        CompanyDto companyDto = modelMapper.map(careerHistory.getCompany(), CompanyDto.class);
                        if(careerHistory.getCompany().getLogoImageName() != null){
                            companyDto.setLogoImageUrl(storageService.getImageByName(careerHistory.getCompany().getLogoImageName()));
                        }
                        careerHistoryDto.setCompanyDto(companyDto);
                        return careerHistoryDto;

                    }
            ).collect(Collectors.toList());

            existProfileDto.setCareerHistoryDtoList(careerHistoryDtoList);
        }

        Response response = Response.builder()
                .statusCode(HttpStatus.OK.value())
                .message("successfully showed existed profile data..")
                .profileDto(existProfileDto)
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Response> getProfileByProfileUrlLink(String profileUrl, HttpServletRequest request) throws IOException {

        User user = getUserByRequest.getUser(request);
        Profile profile = profileRepository.findByUser(user);
        if(profile.getProfileLinkUrl().equals(baseProfileUrl+profileUrl)){
            return showUserProfile(request);
        }
        else {
            Profile otherProfile = profileRepository.findByProfileLinkUrl(baseProfileUrl+profileUrl);
            if(otherProfile == null){throw new ProfileNotFoundException("profile cannot found by url : "+profile.getProfileLinkUrl());}
            return otherProfileService.showOtherUserProfile(otherProfile, profile);
        }
    }
}
