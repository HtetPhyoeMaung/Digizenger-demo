package com.edusn.Digizenger.Demo.post.dto;

import com.edusn.Digizenger.Demo.auth.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.net.URL;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String userProfileUrlLink;
    private int followers;
    private String profileImage;

}
