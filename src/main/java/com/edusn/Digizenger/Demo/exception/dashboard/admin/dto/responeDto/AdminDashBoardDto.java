package com.edusn.Digizenger.Demo.exception.dashboard.admin.dto.responeDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AdminDashBoardDto {

    private Long totalUsers = 0L;

    private Long newUsers = 0L;

    private Long verifiedUsers = 0L;

    private Long suspendedUsers = 0L;

    private Long bannedUsers = 0L;

    private Long inactiveUsers = 0L;

    private List<UserDtoForDashBoard> userDtoForDashBoard;


}
