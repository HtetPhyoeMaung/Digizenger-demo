package com.edusn.Digizenger.Demo.chat.entity;

import com.edusn.Digizenger.Demo.auth.entity.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

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
//        @Transient
//        private Long sendId;
        @ManyToOne(fetch = FetchType.EAGER)
        @JoinColumn(name = "user_id")
        private User user;
        public enum Type{
            IMAGE,VIDEO,VOICE,TEXT
        }

        @OneToMany(mappedBy = "singleChatMessage", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
        private List<Reaction> reactions = new LinkedList<>();



}
