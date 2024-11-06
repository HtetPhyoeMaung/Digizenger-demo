package com.edusn.Digizenger.Demo.utilis;

import com.edusn.Digizenger.Demo.auth.entity.Role;
import com.edusn.Digizenger.Demo.auth.entity.User;
import com.edusn.Digizenger.Demo.notification.dto.NotificationDto;
import com.edusn.Digizenger.Demo.notification.entity.Notification;
import com.edusn.Digizenger.Demo.post.dto.PostDto;
import com.edusn.Digizenger.Demo.post.dto.UserDto;
import com.edusn.Digizenger.Demo.post.entity.Post;
import com.edusn.Digizenger.Demo.profile.dto.response.myProfile.*;
import com.edusn.Digizenger.Demo.profile.entity.Present;
import com.edusn.Digizenger.Demo.profile.entity.Profile;
import com.edusn.Digizenger.Demo.profile.entity.career_history.CareerHistory;
import com.edusn.Digizenger.Demo.profile.entity.education_history.EducationHistory;
import com.edusn.Digizenger.Demo.profile.entity.education_history.School;
import com.edusn.Digizenger.Demo.storage.StorageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
@Slf4j
@RequiredArgsConstructor
public class MapperUtil {
    private final DateUtil dateUtil;
    private final StorageService storageService;
    public  PostDto convertToPostDto(Post post) {
        PostDto postDto = new PostDto();
        postDto.setId(post.getId());
        postDto.setDescription(post.getDescription());
        postDto.setCreatedDate(post.getCreatedDate());
        postDto.setModifiedDate(post.getModifiedDate());
        postDto.setPostType(post.getPostType());
        postDto.setPostLinkUrl(post.getPostLinkUrl());
        return postDto;
    }
    public  UserDto convertToUserDto(User user, boolean includeProfileDto) {
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setEmail(user.getEmail());
        userDto.setGender(String.valueOf(user.getGender()));
        userDto.setDateOfBirth(user.getDateOfBirth());
        userDto.setAddress(user.getAddress());
        userDto.setRole(Role.valueOf(user.getRole()));
        userDto.setFirstName(user.getFirstName());
        userDto.setLastName(user.getLastName());
        userDto.setStatus(user.getStatus());
        userDto.setLastLoginTime(dateUtil.formattedDate(user.getLastLoginTime()));
        if(includeProfileDto && user.getProfile()!=null){
            userDto.setProfileDto(convertToProfileDto(user.getProfile()));
        }

        return userDto;
    }

    public  ProfileDto convertToProfileDto(Profile profile){
        ProfileDto profileDto = new ProfileDto();
        profileDto.setId(profile.getId());
        profileDto.setFollowerCount((long) profile.getFollowers().size());
        profileDto.setProfileImageUrl(profileDto.getProfileImageName()!=null?storageService.getImageByName(profileDto.getProfileImageName()):"");
        profileDto.setCoverImageUrl(profileDto.getCoverImageName()!=null?storageService.getImageByName(profileDto.getCoverImageName()):"");
        return profileDto;
    }

    public  NotificationDto convertToNotificationDto(Notification notification){
        NotificationDto notificationDto=new NotificationDto();
        notificationDto.setId(notification.getId());
        notificationDto.setRead(notification.isRead());
        notificationDto.setMessage(notification.getMessage());
        notificationDto.setType(notification.getType());
        if(notification.getPost()!=null){
            PostDto postDto=convertToPostDto(notification.getPost());
            notificationDto.setPostDto(postDto);

        }
        ProfileDto profileDto=convertToProfileDto(notification.getProfile());
        notificationDto.setProfileDto(profileDto);
        notificationDto.setUserId(notification.getUser().getId());
        return notificationDto;
    }

    public  CareerHistoryDto convertToCareerHistoryDto(CareerHistory careerHistory){
        CompanyDto companyDto = CompanyDto.builder()
                .id(careerHistory.getCompany().getId())
                .companyName(careerHistory.getCompany().getCompanyName())
                .build();

        CareerHistoryDto careerHistoryDto = CareerHistoryDto.builder()
                .id(careerHistory.getId())
                .designation(careerHistory.getDesignation())
                .companyDto(companyDto)
                .joinDate(careerHistory.getJoinDate())
                .build();

        if(careerHistory.getEndDate() != null){
            careerHistoryDto.setEndDate(careerHistory.getEndDate());
            careerHistoryDto.setPresent(careerHistory.getPresent());
        }else{
            careerHistoryDto.setPresent(careerHistory.getPresent());
        }

        return careerHistoryDto;
    }

    public  EducationHistoryDto convertToEducationHistoryDto(EducationHistory educationHistory){
        SchoolDto schoolDto = SchoolDto.builder()
                .id(educationHistory.getSchool().getId())
                .schoolName(educationHistory.getSchool().getSchoolName())
                .build();

        EducationHistoryDto educationHistoryDto = EducationHistoryDto.builder()
                .id(educationHistory.getId())
                .schoolDto(schoolDto)
                .degreeName(educationHistory.getDegreeName())
                .fieldOfStudy(educationHistory.getFieldOfStudy())
                .joinDate(educationHistory.getJoinDate())
                .build();

        if(educationHistory.getEndDate() != null){
            educationHistoryDto.setEndDate(educationHistory.getEndDate());
            educationHistoryDto.setPresent(educationHistory.getPresent());
        }else{
            educationHistoryDto.setPresent(educationHistory.getPresent());
        }

        return educationHistoryDto;
    }



}
