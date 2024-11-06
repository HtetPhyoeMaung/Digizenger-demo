package com.edusn.Digizenger.Demo.dashboard.dto.responseDto;
import com.edusn.Digizenger.Demo.auth.entity.User;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Component
@JsonInclude(content = JsonInclude.Include.NON_NULL)
public class UserDtoForDashBoard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;

    private String lastName;

    private String email;

    private String phone;

    private String role;

    private boolean verified;

    private boolean inactive;

    private boolean suspended;

    private boolean banned;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private LocalDate dateOfBirth;

    private User.Gender gender;
    private LocalDateTime createdDate;

    private String country;

    private ProfileDtoForDashBoard profileDtoForDashBoard;



}
