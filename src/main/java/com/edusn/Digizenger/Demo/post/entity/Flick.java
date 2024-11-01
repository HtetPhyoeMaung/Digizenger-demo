package com.edusn.Digizenger.Demo.post.entity;

import com.edusn.Digizenger.Demo.auth.entity.User;
import jakarta.persistence.*;
import lombok.*;


import java.util.UUID;

@Entity
@Data
@Builder
@AllArgsConstructor
public class Flick {
    @Id
    private String id;

    @ManyToOne(fetch = FetchType.LAZY) // Assuming a single Post can have multiple Flicks
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    private String flickedPostLinkUrl;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL) // Adjust cascade type as needed
    @JoinColumn(name = "flickUser_id")
    private User user;

    public Flick(){
        id= UUID.randomUUID().toString();
    }


}
