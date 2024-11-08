package com.edusn.Digizenger.Demo.chat.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReactionRequest {
    private ChatType chatType;
    private String messageId;
    private String emojiUtf8;
    private Long userId;

    public enum ChatType{
        SINGLE,
        GROUP
    }
}
