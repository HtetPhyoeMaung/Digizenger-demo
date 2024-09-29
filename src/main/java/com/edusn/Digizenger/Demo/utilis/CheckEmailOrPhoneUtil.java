package com.edusn.Digizenger.Demo.utilis;

import com.edusn.Digizenger.Demo.auth.entity.User;
import com.edusn.Digizenger.Demo.exception.UserNotFoundException;
import com.edusn.Digizenger.Demo.auth.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CheckEmailOrPhoneUtil {
    @Autowired
    private UserRepository userRepository;
    public  User checkEmailOrPhone(String emailOrPhone){
        User user;
        if (emailOrPhone.matches(".*@.*")) {
            user = userRepository.findByEmail(emailOrPhone).orElseThrow(() ->
                    new UserNotFoundException("User not found by this email: " + emailOrPhone));
        } else {
            user = userRepository.findByPhone(emailOrPhone).orElseThrow(() ->
                    new UserNotFoundException("User not found by this phone: " + emailOrPhone));
        }

        return user;
    }
}
