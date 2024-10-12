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
    private String firstName;
    private String lastName;
    private String role;
    private String profileLink;
    private boolean verified ;
}
