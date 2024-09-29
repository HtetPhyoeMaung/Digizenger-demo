package com.edusn.Digizenger.Demo.post.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Entity
@Data
@Table(name = "media")
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class Media implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String mediaUrl;
    private MediaType mediaType;
    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;


    public enum MediaType {
        IMAGE,
        VIDEO
    }
}
