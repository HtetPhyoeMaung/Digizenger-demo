package com.edusn.Digizenger.Demo.profile.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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

    @Column(length = 512)
    private String companyLogoUrl;

    @Column(nullable = false)
    private LocalDate joinDate;

    @Column(nullable = false)
    private LocalDate endDate;

    @Enumerated(EnumType.STRING)
    private Present present;

    @ManyToMany(mappedBy = "careerHistoryList")
    private List<Profile> profileList = new ArrayList<>();
}
