package com.edusn.Digizenger.Demo.checkUser.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CheckUserDto {
    private Long id;
    private Long profileId;
    private String firstName;
    private String lastName;
    private String username;
    private String role;
    private boolean verified ;
}
