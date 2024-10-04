//package com.edusn.Digizenger.Demo.profile.entity;
//
//import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
//import jakarta.persistence.*;
//import lombok.AllArgsConstructor;
//import lombok.Builder;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//
//import java.util.LinkedList;
//import java.util.List;
//
//@Entity
//@Data
//@AllArgsConstructor
//@NoArgsConstructor
//@Builder
//@Table(name = "academicInstitutions")
//public class AcademicInstitution {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//    @Enumerated(EnumType.STRING)
//    private AcademicType AcademicType;
//    private String name;
//    private String logoImageName;
//    private String city;
//
//    @OneToMany(mappedBy = "academicInstitution", fetch = FetchType.EAGER)
//    private EducationHistory educationHistory;
//
//    @ManyToMany(mappedBy = "academicInstitutionList")
//    @JsonIgnoreProperties("profileList")
//    private List<Profile> profileList = new LinkedList<>();
//}