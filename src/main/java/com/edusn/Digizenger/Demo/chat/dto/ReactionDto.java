package com.edusn.Digizenger.Demo.chat.dto;

import com.edusn.Digizenger.Demo.post.dto.UserDto;
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
public class ReactionDto {

    private String id;
    private String emoji;
    private String createdDate;
    private String editedDate;
    private UserDto userDto;
    private boolean isReacted;
}
