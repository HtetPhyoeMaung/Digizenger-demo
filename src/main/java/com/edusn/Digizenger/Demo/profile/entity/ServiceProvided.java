package com.edusn.Digizenger.Demo.profile.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    private List<Profile> profileList = new LinkedList<>();
}
