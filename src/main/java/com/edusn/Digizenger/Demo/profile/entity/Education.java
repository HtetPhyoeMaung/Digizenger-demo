package com.edusn.Digizenger.Demo.profile.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    @Enumerated(EnumType.STRING)
    private Type type;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String degreeOrDiplomaName;

    @Column(name = "university_logo_url")
    private String logoName;

    private String logoUrl;

    @Column(nullable = false)
    private LocalDate joinDate;

    @Column(nullable = false)
    private LocalDate endDate;

    @Enumerated(EnumType.STRING)
    private Present present;

    @ManyToMany(mappedBy = "educationList")
    private Set<Profile> profileList = new HashSet<>();

    public enum Type{
        UNIVERSITY,
        COLLAGE,
        SCHOOL
    }

}
