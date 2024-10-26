package com.edusn.Digizenger.Demo.checkUser.service.impl;

import com.edusn.Digizenger.Demo.auth.dto.response.Response;
import com.edusn.Digizenger.Demo.auth.entity.User;
import com.edusn.Digizenger.Demo.checkUser.dto.CheckUserDto;
import com.edusn.Digizenger.Demo.checkUser.service.CheckUserService;
import com.edusn.Digizenger.Demo.utilis.GetUserByRequest;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CheckUserServiceImpl implements CheckUserService {

    private final GetUserByRequest getUserByRequest;

    @Override
    public ResponseEntity<Response> checkUser(HttpServletRequest request) {
        User user = getUserByRequest.getUser(request);
        CheckUserDto checkUserDto = CheckUserDto.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .role(user.getRole())
                .username(user.getProfile().getUsername())
                .verified(user.getVerified())
                .build();

        Response response = Response.builder()
                .statusCode(HttpStatus.OK.value())
                .message("successfully checked user")
                .checkUserDto(checkUserDto)
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
