package com.edusn.Digizenger.Demo.chat.entity;

import com.edusn.Digizenger.Demo.auth.entity.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@Builder
@AllArgsConstructor
@Table(name="group_chat_message")
public class GroupChatMessage {
    @Id
    private String id;
    private String message;
    private LocalDateTime createDate;
    private LocalDateTime modifiedDate;
    private Type type;
    public enum Type{
        IMAGE,VIDEO,TEXT,VOICE
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "group_room_id")
    private GroupRoom groupRoom;

    @OneToMany(mappedBy = "groupChatMessage", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Reaction> reactions = new LinkedList<>();

    public GroupChatMessage(){
        this.id = UUID.randomUUID().toString();
    }
}
