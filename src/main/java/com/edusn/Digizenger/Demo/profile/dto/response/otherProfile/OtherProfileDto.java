package com.edusn.Digizenger.Demo.profile.dto.response.otherProfile;

import com.edusn.Digizenger.Demo.profile.dto.response.myProfile.AboutDto;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OtherProfileDto {
    private Long id;
    private String profileCareer;
    private String profileImgUrl;
    private String coverImageUrl;
    private String username;
    private String profileLinkUrl;
    private String bio;
    private OtherUserProfileDto otherUserProfileDto;
    private AboutDto aboutDto;
}
