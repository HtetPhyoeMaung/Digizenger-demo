package com.edusn.Digizenger.Demo.profile.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Table(name = "about")
public class About {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Set<String> servicesProvides;

    @OneToOne
    @JoinColumn(name = "profile_id")
    private Profile profile;

    @OneToMany(mappedBy = "about", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private CareerHistory careerHistory;

    @OneToMany(mappedBy = "about", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Education education;
}
