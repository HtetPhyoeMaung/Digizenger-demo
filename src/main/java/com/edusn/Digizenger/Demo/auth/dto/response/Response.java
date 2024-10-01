package com.edusn.Digizenger.Demo.auth.dto.response;

import com.edusn.Digizenger.Demo.auth.entity.User;
import com.edusn.Digizenger.Demo.post.dto.PostDto;
import com.edusn.Digizenger.Demo.post.dto.UserDto;
import com.edusn.Digizenger.Demo.post.entity.Post;
import com.edusn.Digizenger.Demo.profile.dto.response.myProfile.ProfileDto;
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

    private PostDto postDto;


    private String expirationDate;

    private ProfileDto profileDto;

    private OtherProfileDto otherProfileDto;

}
