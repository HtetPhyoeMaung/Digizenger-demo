package com.edusn.Digizenger.Demo.utilis;

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
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
@Slf4j
public class MapperUtil {

    public static PostDto convertToPostDto(Post post) {
        log.info("Reach convert TO Post Dto");
        PostDto postDto = new PostDto();
        postDto.setId(post.getId());
        postDto.setDescription(post.getDescription());
        postDto.setCreatedDate(post.getCreatedDate());
        postDto.setModifiedDate(post.getModifiedDate());
        postDto.setPostType(post.getPostType());
        postDto.setPostLinkUrl(post.getPostLinkUrl());
        log.info("Reach 1");
        return postDto;
    }
    public static UserDto convertToUserDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setFirstName(user.getFirstName());
        userDto.setLastName(user.getLastName());
        userDto.setStatus(user.getStatus());

        return userDto;
    }

    public static ProfileDto convertToProfileDto(Profile profile){
        ProfileDto profileDto = new ProfileDto();
        profileDto.setId(profile.getId());
        profileDto.setFollowerCount((long) profile.getFollowers().size());
        return profileDto;
    }

    public static NotificationDto convertToNotificationDto(Notification notification){
        NotificationDto notificationDto=new NotificationDto();
        notificationDto.setId(notification.getId());
        notificationDto.setRead(notification.isRead());
        notificationDto.setMessage(notification.getMessage());
        notificationDto.setType(notification.getType());
        if(notification.getPost()!=null){
            PostDto postDto=MapperUtil.convertToPostDto(notification.getPost());
            notificationDto.setPostDto(postDto);

        }
        ProfileDto profileDto=MapperUtil.convertToProfileDto(notification.getProfile());
        notificationDto.setProfileDto(profileDto);
        notificationDto.setUserId(notification.getUser().getId());
        return notificationDto;
    }

    public static CareerHistoryDto convertToCareerHistoryDto(CareerHistory careerHistory){
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

    public static EducationHistoryDto convertToEducationHistoryDto(EducationHistory educationHistory){
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
