package com.edusn.Digizenger.Demo.profile.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.util.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "servicesProvided")
public class ServiceProvided {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String service;

    @ManyToMany(mappedBy = "serviceProvidedList")
    @JsonIgnoreProperties("serviceProvidedList")
    private List<Profile> profileList = new LinkedList<>();

}
