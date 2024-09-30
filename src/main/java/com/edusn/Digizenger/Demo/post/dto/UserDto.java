package com.edusn.Digizenger.Demo.post.dto;

import com.edusn.Digizenger.Demo.auth.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private String firstName;

    private String lastName;
    private int followers;

}
