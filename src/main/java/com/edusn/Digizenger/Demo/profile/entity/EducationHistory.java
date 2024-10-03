package com.edusn.Digizenger.Demo.profile.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Table(name = "educations")
public class EducationHistory {

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

    private LocalDate endDate;

    @Enumerated(EnumType.STRING)
    private Present present;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "profile_id")
    private Profile profile;

    public enum Type{
        UNIVERSITY,
        COLLAGE,
        SCHOOL
    }

}
