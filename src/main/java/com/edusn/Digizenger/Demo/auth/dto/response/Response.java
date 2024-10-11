package com.edusn.Digizenger.Demo.auth.dto.response;

import com.edusn.Digizenger.Demo.chat.dto.GroupRoomDto;
import com.edusn.Digizenger.Demo.checkUser.dto.CheckUserDto;
import com.edusn.Digizenger.Demo.dashboard.admin.dto.responeDto.AdminDashBoardDto;
import com.edusn.Digizenger.Demo.dashboard.admin.dto.responeDto.showUser.UserDataDto;
import com.edusn.Digizenger.Demo.post.dto.LikeDto;
import com.edusn.Digizenger.Demo.post.dto.PostDto;
import com.edusn.Digizenger.Demo.post.dto.UserDto;
import com.edusn.Digizenger.Demo.profile.dto.response.myProfile.ProfileDto;
import com.edusn.Digizenger.Demo.profile.dto.response.myProfile.ServiceProvidedDto;
import com.edusn.Digizenger.Demo.profile.dto.response.otherProfile.RelationShipDto;
import com.edusn.Digizenger.Demo.profile.dto.response.otherProfile.OtherProfileDto;
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

    private String otp;
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

    private ProfileDto profileDto;

    private OtherProfileDto otherProfileDto;
    private List<RelationShipDto> relationShipDtoList;

    private List<ServiceProvidedDto> serviceProvidedDtoList;
    private AdminDashBoardDto adminDashBoardDto;

    private CheckUserDto checkUserDto;

    private UserDataDto userDataDto;

}
