package com.edusn.Digizenger.Demo.auth.entity;

import com.edusn.Digizenger.Demo.chat.entity.GroupChatMessage;
import com.edusn.Digizenger.Demo.chat.entity.GroupRoom;
import com.edusn.Digizenger.Demo.chat.entity.SingleChatMessage;
import com.edusn.Digizenger.Demo.chat.entity.SingleChatRoom;
import com.edusn.Digizenger.Demo.notification.entity.Notification;
import com.edusn.Digizenger.Demo.post.entity.*;
import com.edusn.Digizenger.Demo.profile.entity.Profile;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users")
@ToString(exclude = {"profile"})
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

    private boolean inactive;

    private boolean suspended;

    private boolean banned;

    private LocalDateTime suspensionDate;

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

    @OneToMany( cascade = CascadeType.ALL, orphanRemoval = true,mappedBy = "user")
    private List<Post> posts;

    @OneToMany
    private List<Flick> flicks;

    @OneToOne(cascade = CascadeType.ALL,orphanRemoval = true)
    @JoinColumn(name = "address_id")
    private Address address;



    @OneToMany(cascade = CascadeType.ALL,orphanRemoval = true,mappedBy = "user")
    private List<Like> likes;



    @OneToOne( cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "user")
    private Profile profile;

    @OneToMany( cascade = CascadeType.ALL, orphanRemoval = true,mappedBy = "user")
    private List<SingleChatMessage> singleChatMessages;

    @OneToMany( cascade = CascadeType.ALL, orphanRemoval = true,mappedBy = "user")
    private List<SingleChatRoom> singleChatRooms;

    @OneToMany( cascade = CascadeType.ALL, orphanRemoval = true,mappedBy = "user")
    private List<GroupChatMessage> groupChatMessages;

    @OneToMany( cascade = CascadeType.ALL, orphanRemoval = true,mappedBy = "user")
    private List<Notification> notificationList;

    @ManyToMany(cascade =CascadeType.ALL)
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
