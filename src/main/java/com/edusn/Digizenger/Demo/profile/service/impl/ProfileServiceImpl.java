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
        Profile profile = user.getProfile();


//        ProfileDto existProfileDto = modelMapper.map(profile, ProfileDto.class);
//        UserForProfileDto userForProfileDto = modelMapper.map(profile.getUser(), UserForProfileDto.class);
        UserDto userDto=mapperUtil.convertToUserDto(user,true);




        /** Service Provided **/
        if(!profile.getServiceProvidedList().isEmpty()){
            List<ServiceProvidedDto> serviceProvidedDtoList = profile.getServiceProvidedList().stream().map(
                    serviceProvided -> modelMapper.map(serviceProvided, ServiceProvidedDto.class)
            ).collect(Collectors.toList());
            userDto.getProfileDto().setServiceProvidedDtoList(serviceProvidedDtoList);
        }

        /** Service **/
        if(!profile.getFollowers().isEmpty()){
            userDto.getProfileDto().setFollowerCount(Long.valueOf(profile.getFollowers().size()));
        }

        /** Following **/
        if(!profile.getFollowing().isEmpty()){
            userDto.getProfileDto().setFollowingCount(Long.valueOf(profile.getFollowing().size()));
        }

        /** Neighbors **/
        if(!profile.getNeighbors().isEmpty()){
            userDto.getProfileDto().setNeighborCount(Long.valueOf(profile.getNeighbors().size()));
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

            userDto.getProfileDto().setEducationHistoryDtoList(educationHistoryDtoList);
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
        Profile profile = profileRepository.findByUser(user);
        if(profile.getUsername().equals(username)){
            return showUserProfile(request);
        }
        else {
            Profile otherProfile = profileRepository.findByUsername(username)
                    .orElseThrow(()-> new CustomNotFoundException("profile not found by url which have username : "+username));
            return otherProfileService.showOtherUserProfile(otherProfile, profile);
        }
    }

    @Override
    public ResponseEntity<Response> getProfileById(HttpServletRequest request, Long id) throws IOException {
        User user = getUserByRequest.getUser(request);
        Profile profile = profileRepository.findByUser(user);
        Profile toFindProfile = profileRepository.findById(id)
                .orElseThrow(() -> new CustomNotFoundException("profile is not exists by id : "+id));
        return otherProfileService.showOtherUserProfile(toFindProfile, profile);
    }
}
