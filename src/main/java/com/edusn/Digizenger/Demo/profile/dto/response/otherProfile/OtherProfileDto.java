package com.edusn.Digizenger.Demo.profile.dto.response.otherProfile;

import com.edusn.Digizenger.Demo.profile.dto.response.myProfile.AboutDto;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.net.URL;

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
    private String coverImageName;
    private URL coverImageUrl;
    private String username;
    private String profileLinkUrl;
    private String bio;
    private OtherUserForProfileDto otherUserForProfileDto;
    private AboutDto aboutDto;
}
