package com.edusn.Digizenger.Demo.profile.entity;

import com.edusn.Digizenger.Demo.auth.entity.User;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.*;

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

    @Column(name = "profile_image_name")
    private String profileImageName;

    @Column(name = "cover_image_name")
    private String coverImageName;

    private String username;

    @Column(name = "profile_link_url", unique = true)
    private String profileLinkUrl;

    private String bio;

    @OneToOne(fetch = FetchType.EAGER , cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "userId")
    private User user;

    @OneToMany(mappedBy = "profile", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CareerHistory> careerHistoryList = new LinkedList<>();

    @ManyToMany
    @JoinTable(
            name = "profile_education",
            joinColumns = @JoinColumn(name = "profile_id"),
            inverseJoinColumns = @JoinColumn(name = "education_id")
    )
    private List<EducationHistory> educationHistoryList = new LinkedList<>();

    @ManyToMany
    @JoinTable(
            name = "profile_serviceProvided",
            joinColumns = @JoinColumn(name = "profile_id"),
            inverseJoinColumns = @JoinColumn(name = "service_provided_id")
    )
    @JsonIgnoreProperties("profileList")
    private List<ServiceProvided> serviceProvidedList = new LinkedList<>();

    @ManyToMany
    @JoinTable(
            name = "profile_academicInstitutionList",
            joinColumns = @JoinColumn(name = "profile_id"),
            inverseJoinColumns = @JoinColumn(name = "academicInstitution_id")
    )
    @JsonIgnoreProperties("academicInstitutionList")
    private List<AcademicInstitution> academicInstitutionList = new LinkedList<>();

    public void addServiceProvided(ServiceProvided serviceProvided){
        this.serviceProvidedList.add(serviceProvided);
        serviceProvided.getProfileList().add(this);
    }

    public void removeProvided(ServiceProvided serviceProvided){
        this.serviceProvidedList.remove(serviceProvided);
        serviceProvided.getProfileList().remove(this);
    }

    public void addAcademicInstitution(AcademicInstitution academicInstitution){
        this.academicInstitutionList.add(academicInstitution);
        academicInstitution.getProfileList().add(this);
    }

    public void removeAcademicInstitution(AcademicInstitution academicInstitution){
        this.academicInstitutionList.remove(academicInstitution);
        academicInstitution.getProfileList().remove(this);
    }
}
