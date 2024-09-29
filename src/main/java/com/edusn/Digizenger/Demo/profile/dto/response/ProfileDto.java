package com.edusn.Digizenger.Demo.profile.dto.response;

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
    private String profileImgUrl;
    private String coverImageUrl;
    private String bio;
    private UserProfileDto userProfileDto;
    private AboutDto aboutDto;
}
