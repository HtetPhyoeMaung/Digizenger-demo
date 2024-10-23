package com.edusn.Digizenger.Demo.utilis;

import com.edusn.Digizenger.Demo.auth.entity.User;
import com.edusn.Digizenger.Demo.auth.repo.UserRepository;
import com.edusn.Digizenger.Demo.exception.UserNotFoundException;
import com.edusn.Digizenger.Demo.security.JWTService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GetUserByRequest {

    private final JWTService jwtService;
    private final UserRepository userRepository;
    private final CheckEmailOrPhoneUtil checkEmailOrPhoneUtil;

    public  User getUser(HttpServletRequest request){
        /* Get Token form Header */
        String token = jwtService.getJWTFromRequest(request);

        /* Get Email from token */
        String emailOrPhone = jwtService.extractUsername(token);
        /* Get user find by email */

        return checkEmailOrPhoneUtil.checkEmailOrPhone(emailOrPhone);
    }
}
