package com.edusn.Digizenger.Demo.dashboard.dto.responseDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProfileDtoForDashBoard {

    private Long id;

    private  String  profileImageUrl;

    private String username;
}
