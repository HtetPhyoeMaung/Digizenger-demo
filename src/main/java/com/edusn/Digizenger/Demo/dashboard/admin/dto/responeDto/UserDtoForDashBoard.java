package com.edusn.Digizenger.Demo.dashboard.admin.dto.responeDto;
import com.edusn.Digizenger.Demo.auth.entity.User;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
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
public class UserDtoForDashBoard {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;

    private String lastName;

    private String email;

    private String phone;

    private String role;

    private Boolean verified;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private LocalDate dateOfBirth;

    private User.Gender gender;
    //    @Column(name = "created_date")
    private LocalDateTime createdDate;

    private String country;

    private ProfileDtoForDashBoard profileDtoForDashBoard;



}
