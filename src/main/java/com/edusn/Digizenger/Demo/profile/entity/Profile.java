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

    @Column(name = "profile_img_url")
    private String profileImgUrl;

    @Column(name = "cover_img_url")
    private String coverImgUrl;

    @Column(length = 255)
    private String bio;

    @Column(name = "profile_img_data")
    private byte[] profileImageData;

    @Column(name = "cover_img_data")
    private byte[] coverImageData;

    @OneToOne(mappedBy = "profile", fetch = FetchType.EAGER , cascade = CascadeType.ALL, orphanRemoval = true)
    private User user;

    @OneToOne(mappedBy = "profile", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private About about;
}
