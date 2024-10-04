package com.edusn.Digizenger.Demo.chat.dto;

import com.edusn.Digizenger.Demo.post.dto.UserDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GroupRoomDto {
    private Long id;
    private String groupName;
    private LocalDateTime createDate;
    private LocalDateTime modifiedDate;
    private List<UserDto> userDtoList;
}
