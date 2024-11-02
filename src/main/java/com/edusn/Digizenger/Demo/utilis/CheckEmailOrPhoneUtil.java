package com.edusn.Digizenger.Demo.utilis;

import com.edusn.Digizenger.Demo.auth.entity.User;
import com.edusn.Digizenger.Demo.auth.repo.UserRepository;
import com.edusn.Digizenger.Demo.exception.CustomNotFoundException;
import com.edusn.Digizenger.Demo.profile.entity.Profile;
import com.edusn.Digizenger.Demo.profile.repo.ProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CheckEmailOrPhoneUtil {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProfileRepository profileRepository;

    public  User checkEmailOrPhone(String emailOrPhone){
        User user;
        if (emailOrPhone.matches(".*@.*")) {
            user = userRepository.findByEmail(emailOrPhone).orElseThrow(() ->
                    new CustomNotFoundException("User not found by this email: " + emailOrPhone));
        } else if(emailOrPhone.matches("\\d+")){
            user = userRepository.findByPhone(emailOrPhone).orElseThrow(() ->
                    new CustomNotFoundException("User not found by this phone: " + emailOrPhone));
        } else {
            Profile profile = profileRepository.findByUsername(emailOrPhone)
                    .orElseThrow(() -> new CustomNotFoundException("Profile cannot found by " + emailOrPhone));
            user = profile.getUser();
        }

        return user;
    }
}
