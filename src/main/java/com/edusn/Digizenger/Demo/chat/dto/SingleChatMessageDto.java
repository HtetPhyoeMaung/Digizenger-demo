package com.edusn.Digizenger.Demo.chat.dto;

import com.edusn.Digizenger.Demo.chat.entity.SingleChatMessage;
import com.edusn.Digizenger.Demo.post.dto.UserDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SingleChatMessageDto {

    private String id;
    private String message;
    private LocalDateTime createDate;
    private LocalDateTime  modifiedDate;
    private String chatId;
    private Long recipientId;
    private boolean isRead;
    private SingleChatMessage.Type type;
    private UserDto userDto;
    private String replyMessage;
    private SingleChatMessage.Type replayMessageType;
    private List<ReactionDto> reactionDtoList;


}
