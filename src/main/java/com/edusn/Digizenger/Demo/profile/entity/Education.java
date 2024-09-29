package com.edusn.Digizenger.Demo.profile.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Table(name = "educations")
public class Education {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String universityName;

    @Column(nullable = false)
    private String degree_name;

    @Column(name = "university_logo_url")
    private String universityLogoUrl;

    @Column(name = "university_logo_data")
    private byte[] universityLogoData;

    @ManyToOne
    @JoinColumn(name = "about_id")
    private About about;
}
