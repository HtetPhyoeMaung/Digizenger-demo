package com.edusn.Digizenger.Demo.chat.entity;

import com.edusn.Digizenger.Demo.auth.entity.User;
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
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "one_to_one_message")
public class SingleChatMessage {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;
        private String message;
        private LocalDateTime createDate;
        private LocalDateTime modifiedDate;
        private String chatId;
        private Long recipientId;
        private Type type;
//        @Transient
//        private Long sendId;
        @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
        @JoinColumn(name = "user_id")
        @JsonIgnore
        private User user;
        public enum Type{
            IMAGE,VIDEO,VOICE,TEXT
        }

}
