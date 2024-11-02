package com.edusn.Digizenger.Demo.dashboard.dto.responseDto;
import com.edusn.Digizenger.Demo.dashboard.dto.responseDto.showUser.UserDataDto;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AdminDashBoardDto {

    private Long totalUsers ;

    private Long newUsers ;

    private Long verifiedUsers ;

    private Long suspendedUsers ;

    private Long bannedUsers ;

    private Long inactiveUsers ;

    private List<UserDtoForDashBoard> userDtoForDashBoard;


}
