package com.edusn.Digizenger.Demo.profile.dto.response.myProfile;

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
    private String profileLinkUrl;
    private String bio;
    private Long followersCount = 0L;
    private Long followingCount = 0L;
    private Long neighborCount = 0L;
    private UserForProfileDto userForProfileDto;
    private List<CareerHistoryDto> careerHistoryDtoList = new LinkedList<>();
    private List<ServiceProvidedDto> serviceProvidedDtoList = new LinkedList<>();
}
