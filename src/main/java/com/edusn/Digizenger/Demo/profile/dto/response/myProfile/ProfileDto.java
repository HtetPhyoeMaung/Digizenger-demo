package com.edusn.Digizenger.Demo.profile.dto.response.myProfile;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.net.URL;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProfileDto {
    private Long id;
    private String profileCareer;
    private String profileImageName;
    private URL profileImageUrl;
    private String coverImageName;
    private URL coverImageUrl;
    private String username;
    private String profileLinkUrl;
    private String bio;
    private UserForProfileDto userForProfileDto;
    private AboutDto aboutDto;
}
