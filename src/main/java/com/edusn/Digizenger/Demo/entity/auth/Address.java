package com.edusn.Digizenger.Demo.entity.auth;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "addresses")
public class Address implements Serializable {
    private static final long serialVersionUID = 1l;

    @Id
    @GeneratedValue(generator = "addressSeq")
    @SequenceGenerator(name = "addressSeq",initialValue = 101,allocationSize = 1)
    private Long id;

    private String country;

    private String city;





}
