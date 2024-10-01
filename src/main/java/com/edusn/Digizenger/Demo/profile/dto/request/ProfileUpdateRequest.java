package com.edusn.Digizenger.Demo.profile.dto.request;

import com.edusn.Digizenger.Demo.auth.entity.User;
import com.edusn.Digizenger.Demo.profile.entity.About;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProfileUpdateRequest {

    private String profileImgUrl;
    private String coverImgUrl;
    private String bio;
    private User user;
    private About about;
}
