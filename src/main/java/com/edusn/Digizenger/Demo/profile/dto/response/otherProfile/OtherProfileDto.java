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
public class OtherProfileDto {
    private Long id;
    private String profileCareer;
    private String profileImageName;
    private String profileImageUrl;
    private String coverImageUrl;
    private String coverImageName;
    private String username;
    private String profileLinkUrl;
    private String bio;
    private Long followerCount = 0L;
    private Long followingCount = 0L;
    private Long neighborCount = 0L;
    private OtherUserForProfileDto otherUserForProfileDto;
    private List<CareerHistoryDto> careerHistoryDtoList = new LinkedList<>();
    private List<ServiceProvidedDto> serviceProvidedDtoList = new LinkedList<>();
}
