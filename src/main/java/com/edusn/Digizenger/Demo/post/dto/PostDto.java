package com.edusn.Digizenger.Demo.post.dto;

import com.edusn.Digizenger.Demo.post.entity.Post;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PostDto {

    private Long id;
    private String description;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;
    private Post.PostType postType;
    private String imageName;
    private UserDto userDto;
    private Long likeCount;
    private Long viewCount;

}
