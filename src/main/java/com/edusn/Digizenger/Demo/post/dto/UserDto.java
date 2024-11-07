package com.edusn.Digizenger.Demo.post.dto;

import com.edusn.Digizenger.Demo.auth.entity.Address;
import com.edusn.Digizenger.Demo.auth.entity.Role;
import com.edusn.Digizenger.Demo.auth.entity.User;
import com.edusn.Digizenger.Demo.profile.dto.response.myProfile.ProfileDto;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String gender;
    private LocalDate dateOfBirth;
    private Address address;
    private String lastLoginTime;
    private User.Status status;
    private Role role;
    private ProfileDto profileDto;
    private String lastMessage;


}
