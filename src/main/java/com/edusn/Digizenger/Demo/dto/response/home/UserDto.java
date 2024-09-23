package com.edusn.Digizenger.Demo.dto.response.home;

import com.edusn.Digizenger.Demo.entity.auth.Role;
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
    private Role role;

}
