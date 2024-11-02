package com.edusn.Digizenger.Demo.dashboard.dto.responseDto.showUser;

import com.edusn.Digizenger.Demo.auth.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDataDto {

    private Long id;

    private String firstName;

    private String lastName;

    private String email;

    private String phone;

    private String password;

    private String role;

    private Boolean verified;

    private boolean locked;

    private boolean activated;

    private LocalDateTime validPassDate;

    private LocalDateTime retiredDate;

    private LocalDate dateOfBirth;

    private User.Gender gender;

    private LocalDateTime createdDate;

    private LocalDateTime lastLoginTime;

    private ProfileDataDto profileDataDto;


}
