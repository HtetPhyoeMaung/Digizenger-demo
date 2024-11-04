package com.edusn.Digizenger.Demo.chat.entity;

import com.edusn.Digizenger.Demo.auth.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@Builder
@Table(name = "reactions")
public class Reaction {
    @Id
    private String id;

    private boolean isReacted;
    private String emoji;
    private LocalDateTime createdDate;
    private LocalDateTime editedDate;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "single_chat_id")
    private SingleChatMessage singleChatMessage;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "group_chat_id")
    private GroupChatMessage groupChatMessage;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private User user;

    public Reaction(){
        this.id = UUID.randomUUID().toString();
    }

}
