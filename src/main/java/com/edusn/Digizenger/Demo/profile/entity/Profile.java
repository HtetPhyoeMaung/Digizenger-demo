package com.edusn.Digizenger.Demo.profile.entity;

import com.edusn.Digizenger.Demo.auth.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Table(name = "profiles")
public class Profile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String profileCareer;

    @Column(name = "profile_img_url")
    private String profileImgUrl;

    @Column(name = "cover_img_url")
    private String coverImgUrl;

    private String username;

    @Column(name = "profile_link_url")
    private String profileLinkUrl;

    @Column(length = 255)
    private String bio;

    @OneToOne(fetch = FetchType.EAGER , cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "userId")
    private User user;

    @OneToOne(mappedBy = "profile", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private About about;
}
