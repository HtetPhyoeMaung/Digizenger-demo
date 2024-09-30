package com.edusn.Digizenger.Demo.profile.dto.response.myProfile;

import com.edusn.Digizenger.Demo.auth.entity.Address;
import com.edusn.Digizenger.Demo.post.dto.PostDto;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserForProfileDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String gender;
    private LocalDate dateOfBirth;
    private LocalDateTime createdDate;
    private Address address;
    private List<PostDto> postDtoList;

}
