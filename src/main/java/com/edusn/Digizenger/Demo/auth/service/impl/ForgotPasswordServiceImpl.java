package com.edusn.Digizenger.Demo.auth.service.impl;

import com.edusn.Digizenger.Demo.auth.dto.response.Response;
import com.edusn.Digizenger.Demo.auth.entity.User;
import com.edusn.Digizenger.Demo.auth.repo.UserRepository;
import com.edusn.Digizenger.Demo.auth.service.ForgotPasswordService;
import com.edusn.Digizenger.Demo.checkUser.dto.CheckUserDto;
import com.edusn.Digizenger.Demo.utilis.CheckEmailOrPhoneUtil;
import com.edusn.Digizenger.Demo.utilis.GetUserByRequest;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ForgotPasswordServiceImpl implements ForgotPasswordService {

    private final CheckEmailOrPhoneUtil checkEmailOrPhoneUtil;
    private final UserRepository userRepository;
    private final GetUserByRequest getUserByRequest;
    private final PasswordEncoder passwordEncoder;

    @Override
    public ResponseEntity<Response> checkUserByEmailOrPhoneOrUsername(String emailOrPhoneOrUsername) {

        User user = checkEmailOrPhoneUtil.checkEmailOrPhone(emailOrPhoneOrUsername);
        CheckUserDto checkUserDto = CheckUserDto.builder()
                .id(user.getId())
                .profileId(user.getProfile().getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .role(user.getRole())
                .username(user.getProfile().getUsername())
                .verified(user.getVerified())
                .build();

        if(user.getEmail() != null) checkUserDto.setEmail(user.getEmail());
        if(user.getPhone() != null) checkUserDto.setPhone(user.getPhone());

        Response response = Response.builder()
                .statusCode(HttpStatus.OK.value())
                .message("successfully got user for forgot password.")
                .checkUserDto(checkUserDto)
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @Override
    public ResponseEntity<Response> resetPassword(HttpServletRequest request, String newPassword, String conformPassword) {
        User user = getUserByRequest.getUser(request);

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        Response response = Response.builder()
                .statusCode(HttpStatus.CREATED.value())
                .message("successfully reset password.")
                .build();
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}
