package com.edusn.Digizenger.Demo.profile.dto.response.myProfile;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProfileDto {
    private Long id;
    private String profileCareer;
    private String profileImageName;
    private byte[] profileImageByte;
    private String coverImageName;
    private byte[] coverImageByte;
    private String username;
    private String profileLinkUrl;
    private String bio;
    private UserForProfileDto userForProfileDto;
    private AboutDto aboutDto;
}
