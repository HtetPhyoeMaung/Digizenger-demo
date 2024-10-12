package com.edusn.Digizenger.Demo.profile.entity.education_history;

import com.edusn.Digizenger.Demo.profile.entity.Profile;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.LinkedList;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "schools")
public class School {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String schoolName;

    private String LogoImageName;

    @OneToMany(mappedBy = "school",fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    private List<EducationHistory> educationHistories;

    @ManyToMany(mappedBy = "schools")
    @JsonIgnoreProperties("schools")
    private List<Profile> profiles = new LinkedList<>();

    public void removeEducationHistory(EducationHistory educationHistory){
        this.getEducationHistories().remove(educationHistory);
        educationHistory.setSchool(null);
    }

}