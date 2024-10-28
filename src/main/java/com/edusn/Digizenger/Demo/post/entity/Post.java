package com.edusn.Digizenger.Demo.post.entity;

import com.edusn.Digizenger.Demo.auth.entity.User;
import com.edusn.Digizenger.Demo.notification.entity.Notification;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "posts")

public class Post implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 20000)
    private String description;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;
    private PostType postType;

    private String imageName;



    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(fetch = FetchType.LAZY,cascade = CascadeType.ALL,orphanRemoval = true,mappedBy = "post")
    private List<Comment> comments;

    @OneToMany(fetch = FetchType.LAZY,cascade = CascadeType.ALL,orphanRemoval = true,mappedBy = "post")
    private List<Like> likes;

    @OneToMany(fetch = FetchType.LAZY,cascade = CascadeType.ALL,orphanRemoval = true,mappedBy = "post")
    private List<View> viewList;

    @OneToMany( fetch = FetchType.LAZY,cascade = CascadeType.ALL ,orphanRemoval = true,mappedBy = "post")
    private List<Notification> notificationList;


   public enum PostType{
       EVERYONE,
       NEIGHBORS,
       FOLLOWERS,

   }

}
