package com.edusn.Digizenger.Demo.profile.dto.response.myProfile;

import com.edusn.Digizenger.Demo.profile.entity.RelationshipStatus;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.net.URL;
import java.util.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProfileDto {
    private Long id;
    private String profileCareer;
    private String profileImageName;
    private String  profileImageUrl;
    private String coverImageName;
    private String coverImageUrl;
    private String username;
    private String bio;
    private Long followerCount;
    private Long followingCount ;
    private Long neighborCount ;
    private RelationshipStatus relationshipStatus;
    private List<CareerHistoryDto> careerHistoryDtoList;
    private List<ServiceProvidedDto> serviceProvidedDtoList;
    private List<EducationHistoryDto> educationHistoryDtoList;
}
