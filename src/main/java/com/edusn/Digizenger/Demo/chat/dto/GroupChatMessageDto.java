package com.edusn.Digizenger.Demo.chat.dto;

import com.edusn.Digizenger.Demo.chat.entity.GroupChatMessage;
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
    private Long id;
    private String text;
    private LocalDateTime createDate;
    private LocalDateTime modifiedDate;
    private String photoOrVideo;
    private GroupChatMessage.Type type;
    public enum Type{
        IMAGE,VIDEO,TEXT,VOICE
    }


}
