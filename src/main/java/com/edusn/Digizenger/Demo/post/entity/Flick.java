package com.edusn.Digizenger.Demo.post.entity;

import com.edusn.Digizenger.Demo.auth.entity.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;


import java.io.Serializable;
import java.util.UUID;

@Entity
@Data
@Builder
@AllArgsConstructor
@Table(name = "flick")
public class Flick implements Serializable {
    private static final long serialVersionUID=1L;
    @Id
    private String id;
    private String flickedPostLinkUrl;
    @ManyToOne()
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @ManyToOne()
    @JoinColumn(name = "flickUser_id")
    private User user;

    public Flick(){
        id= UUID.randomUUID().toString();
    }


}
