package com.edusn.Digizenger.Demo.profile.dto.response.otherProfile;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.net.URL;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RelationShipDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String profileCareer;
    private String profileImageName;
    private String  profileImageUrl;
    private String coverImageName;
    private String  coverImageUrl;
    private String username;
    private String profileLinkUrl;
    private Long followersCount = 0L;
    private Long followingCount = 0L;
    private Long neighborsCount = 0L;
}
