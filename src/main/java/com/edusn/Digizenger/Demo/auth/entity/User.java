package com.edusn.Digizenger.Demo.auth.entity;

import com.edusn.Digizenger.Demo.chat.entity.*;
import com.edusn.Digizenger.Demo.notification.entity.Notification;
import com.edusn.Digizenger.Demo.post.entity.*;
import com.edusn.Digizenger.Demo.profile.entity.Profile;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String firstName;

    private String lastName;
    private String email;
    private String phone;
    @Column(nullable = false)
    private String password;

    private String role;

    private String otp;

    private Boolean verified;

    private boolean activated;

    private boolean locked;


    private LocalDateTime validPassDate;

    private LocalDateTime retiredDate;

    private LocalDateTime otpGeneratedTime;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private LocalDate dateOfBirth;
    private Status status;
    private Gender gender;
//    @Column(name = "created_date")
    private LocalDateTime createdDate;

    private LocalDateTime lastLoginTime;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true,mappedBy = "user")
    private List<Post> posts;

    @OneToMany
    private List<Flick> flicks;

    @OneToOne(fetch = FetchType.EAGER,cascade = CascadeType.ALL,orphanRemoval = true)
    @JoinColumn(name = "address_id")
    private Address address;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true,mappedBy = "user")
    private List<Comment> comments;

    @OneToMany(fetch = FetchType.LAZY,cascade = CascadeType.ALL,orphanRemoval = true,mappedBy = "user")
    private List<Like> likes;

    @OneToMany(fetch = FetchType.LAZY,cascade = CascadeType.ALL,orphanRemoval = true,mappedBy = "user")
    private List<Reply> replies;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "user")
    private Profile profile;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true,mappedBy = "user")
    private List<SingleChatMessage> singleChatMessages;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true,mappedBy = "user")
    private List<SingleChatRoom> singleChatRooms;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true,mappedBy = "user")
    private List<GroupChatMessage> groupChatMessages;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true,mappedBy = "user")
    private List<Notification> notificationList;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "reactedUser")
    private List<Reaction> reactions = new LinkedList<>();



    @ManyToMany(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @JoinTable(name="users_have_groups",joinColumns = @JoinColumn(name="user_id"),
            inverseJoinColumns = @JoinColumn(name = "groups_id")
    )
    private List<GroupRoom> groupRooms = new ArrayList<>();
    public enum Gender{
        MALE,
        FEMALE,
        OTHER,
        PEFERNOTSAY
    }
    public enum Status{
        ONLINE,OFFLINE
    }




}
