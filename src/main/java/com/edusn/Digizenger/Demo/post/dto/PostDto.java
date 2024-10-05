package com.edusn.Digizenger.Demo.post.dto;

import com.edusn.Digizenger.Demo.post.entity.Post;
import com.edusn.Digizenger.Demo.profile.dto.response.myProfile.ProfileDto;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.List;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostDto {

    private Long id;
    private String description;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;
    private Post.PostType postType;
    private String imageName;
    private String  imageUrl;
    private String  vedioUrl;
    private UserDto userDto;
    private ProfileDto profileDto;
    private Long likeCount ;
    private Long viewCount;
    private boolean isLiked;



}
