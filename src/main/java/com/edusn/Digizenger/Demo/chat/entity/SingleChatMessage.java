package com.edusn.Digizenger.Demo.chat.entity;

import com.edusn.Digizenger.Demo.auth.entity.User;
import com.edusn.Digizenger.Demo.utilis.UUIDUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "one_to_one_message")
public class SingleChatMessage {
        @Id
        private String  id;
        private String message;
        private LocalDateTime createDate;
        private LocalDateTime modifiedDate;
        private String chatId;
        private Long recipientId;
        private Type type;
        private boolean isRead;
        private String replyMessageId;
        @ManyToOne(fetch = FetchType.EAGER)
        @JoinColumn(name = "user_id")
        private User user;
        public enum Type{
            IMAGE,VIDEO,VOICE,TEXT
        }


}
