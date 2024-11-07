package com.edusn.Digizenger.Demo.dashboard.dto.responseDto.showUser;
import com.edusn.Digizenger.Demo.profile.dto.response.myProfile.CareerHistoryDto;
import com.edusn.Digizenger.Demo.profile.dto.response.myProfile.ServiceProvidedDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.LinkedList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class ProfileDataDto {

    private Long id;

    private String profileImageUrl;

    private String coverImageUrl;

    private String username;

    private String profileLinkUrl;

    private String bio;

    private Long postCount = 0L;

    private Long followerCount = 0L;

    private Long followingCount = 0L;

    private Long neighborCount = 0L;

    private List<CareerHistoryDto> careerHistoryList = new LinkedList<>();

    private List<ServiceProvidedDto> serviceProvidedList = new LinkedList<>();
}
