//package com.edusn.Digizenger.Demo.profile.entity.career_history;
//
//import com.edusn.Digizenger.Demo.profile.entity.Profile;
//import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
//import jakarta.persistence.*;
//import lombok.AllArgsConstructor;
//import lombok.Builder;
//import lombok.NoArgsConstructor;
//
//import java.util.ArrayList;
//import java.util.LinkedList;
//import java.util.List;
//
//@Entity
//@AllArgsConstructor
//@NoArgsConstructor
//@Builder
//@Table(name = "companies")
//public class Company {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//    private String companyName;
//    private String logoImageName;
//
//    @ManyToMany(mappedBy = "companies")
//    @JsonIgnoreProperties("companies")
//    private List<Profile> profiles = new LinkedList<>();
//
//    @OneToMany(mappedBy = "company", fetch = FetchType.EAGER,  cascade = CascadeType.ALL)
//    private List<CareerHistory> careerHistories = new ArrayList<>();
//
//    public void removeCareerHistory(CareerHistory careerHistory){
//        this.careerHistories.remove(careerHistory);
//        careerHistory.;
//    }
//}
