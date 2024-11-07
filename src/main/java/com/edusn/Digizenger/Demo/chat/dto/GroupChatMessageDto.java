package com.edusn.Digizenger.Demo.chat.dto;

import com.edusn.Digizenger.Demo.auth.entity.User;
import com.edusn.Digizenger.Demo.chat.entity.GroupChatMessage;
import com.edusn.Digizenger.Demo.chat.entity.GroupRoom;
import com.edusn.Digizenger.Demo.post.dto.UserDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GroupChatMessageDto {
    private String id;
    private String message;
    private String createDate;
    private String modifiedDate;
    private GroupChatMessage.Type type;
    private UserDto userDto;
    private GroupRoomDto groupRoomDto;
}
