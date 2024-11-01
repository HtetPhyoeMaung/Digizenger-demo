package com.edusn.Digizenger.Demo.auth.dto.response;

import com.edusn.Digizenger.Demo.chat.dto.GroupRoomDto;
import com.edusn.Digizenger.Demo.chat.dto.SingleChatMessageDto;
import com.edusn.Digizenger.Demo.checkUser.dto.CheckUserDto;
import com.edusn.Digizenger.Demo.dashboard.admin.dto.responseDto.AdminDashBoardDto;
import com.edusn.Digizenger.Demo.dashboard.admin.dto.responseDto.showUser.UserDataDto;
import com.edusn.Digizenger.Demo.notification.dto.NotificationDto;
import com.edusn.Digizenger.Demo.post.dto.LikeDto;
import com.edusn.Digizenger.Demo.post.dto.PostDto;
import com.edusn.Digizenger.Demo.post.dto.UserDto;
import com.edusn.Digizenger.Demo.profile.dto.response.myProfile.*;
import com.edusn.Digizenger.Demo.profile.dto.response.otherProfile.RelationShipDto;
import com.edusn.Digizenger.Demo.profile.dto.response.otherProfile.OtherProfileDto;
import com.edusn.Digizenger.Demo.profile.entity.RelationshipStatus;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.net.URL;
import java.util.List;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Response {

    private int statusCode;

    private String message;

    private String token;

    private String  role;
//hello
    private String otp;
    private Long unreadNotificationCount;
    private List<PostDto> postDtoList;
    private List<UserDto> userDtoList;
    private Long viewCount;
    private URL imageUrl;
    private UserDto userDto;
    private LikeDto likeDto;
    private PostDto postDto;
    private GroupRoomDto groupRoomDto;
    private List<GroupRoomDto>groupRoomDtoList;
    private String expirationDate;
    private List<NotificationDto> notificationDtoList;
    private ProfileDto profileDto;
    private List<SingleChatMessageDto>singleChatMessageDtoList;
    private OtherProfileDto otherProfileDto;
    private List<RelationShipDto> relationShipDtoList;

    private List<ServiceProvidedDto> serviceProvidedDtoList;
    private AdminDashBoardDto adminDashBoardDto;

    private CheckUserDto checkUserDto;

    private UserDataDto userDataDto;

    private CareerHistoryDto careerHistoryDto;

    private EducationHistoryDto educationHistoryDto;

    private List<String> schoolNameList;

    private List<String> companyNameList;

    private ImageDto imageDto;

    private String coverImageUrl;

    private String profileImageUrl;

    private ServiceProvidedDto serviceProvidedDto;

    private RelationshipStatus relationshipStatus;

    private String username;

}
