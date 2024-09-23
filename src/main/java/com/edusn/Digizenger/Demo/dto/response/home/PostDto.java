package com.edusn.Digizenger.Demo.dto.response.home;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    private String content;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;
    private Boolean isPublic;
    private UserDto userDto;
    private List<CommentDto> comments;
    private List<ReplyDto> replies;
    private List<LikeDto> likes;
    private List<MediaDto> mediaDtos;
}
