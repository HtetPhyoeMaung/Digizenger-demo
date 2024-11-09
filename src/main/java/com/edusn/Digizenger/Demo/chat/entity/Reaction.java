package com.edusn.Digizenger.Demo.chat.entity;

import com.edusn.Digizenger.Demo.auth.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "reactions", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"user_id", "single_chat_message_id"})
})
public class Reaction {
    @Id
    private String id;

    private String emoji;
    private LocalDateTime createdDate;
    private LocalDateTime editedDate;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "single_chat_id")
    private SingleChatMessage singleChatMessage;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_chat_id")
    private GroupChatMessage groupChatMessage;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;


}
