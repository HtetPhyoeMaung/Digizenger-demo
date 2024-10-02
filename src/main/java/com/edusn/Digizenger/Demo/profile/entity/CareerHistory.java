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
@Table(name = "career_histories")
public class CareerHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String careerName;

    @Column(nullable = false)
    private String companyName;

    private String companyLogoName;

    @Column(nullable = false)
    private LocalDate joinDate;

    private LocalDate endDate;

    @Enumerated(EnumType.STRING)
    private Present present;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "profile_id")
    private Profile profile;
}
