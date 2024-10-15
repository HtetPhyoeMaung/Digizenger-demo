package com.edusn.Digizenger.Demo.exception.dashboard.admin.dto.responeDto;

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

    private String profileLinkUrl;

}
