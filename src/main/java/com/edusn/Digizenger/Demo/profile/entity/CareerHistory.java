package com.edusn.Digizenger.Demo.profile.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

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

    private LocalDateTime joinDate;
    private LocalDateTime endDate;

    @ManyToOne
    @JoinColumn(name = "about_id")
    private About about;
}
