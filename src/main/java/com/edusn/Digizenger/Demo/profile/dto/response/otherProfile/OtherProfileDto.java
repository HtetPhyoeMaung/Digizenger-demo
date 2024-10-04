package com.edusn.Digizenger.Demo.profile.dto.response.otherProfile;

import com.edusn.Digizenger.Demo.profile.dto.response.myProfile.CareerHistoryDto;
import com.edusn.Digizenger.Demo.profile.dto.response.myProfile.ServiceProvidedDto;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.net.URL;
import java.util.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OtherProfileDto {
    private Long id;
    private String profileCareer;
    private String profileImageName;
    private URL profileImageUrl;
    private URL coverImageUrl;
    private String coverImageName;
    private String username;
    private String profileLinkUrl;
    private String bio;
    private Long follower;
    private Long following;
    private Long neighbors;
    private OtherUserForProfileDto otherUserForProfileDto;
    private List<CareerHistoryDto> careerHistoryDtoList = new LinkedList<>();
//    private List<EducationHistoryDto> educationHistoryDtoList = new LinkedList<>();
    private List<ServiceProvidedDto> serviceProvidedDtoList = new LinkedList<>();
}
