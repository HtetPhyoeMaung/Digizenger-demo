package com.edusn.Digizenger.Demo.profile.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

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
    private String educationQualificationName;
    @Column(nullable = false)
    private LocalDate joinDate;
    private LocalDate endDate;
    @Enumerated(EnumType.STRING)
    private Present present;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "profile_id")
    private Profile profile;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "academicInstitution_id")
    private AcademicInstitution academicInstitution;


}
