package com.edusn.Digizenger.Demo.security;


import com.edusn.Digizenger.Demo.auth.entity.Role;
import com.edusn.Digizenger.Demo.auth.repo.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
@Slf4j
@Service
public class UserDetailServiceForUser implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;
    @Override
    public  UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if(username.matches(".*@.*")){
            return userRepository.findByEmail(username)

                    .map(customer ->
                            User.withUsername(username)
                                    .authorities(Role.USER.name())
                                    .password(customer.getPassword())
                                    .accountExpired(isExpired(customer))
                                    .accountLocked(customer.isLocked())
                                    .credentialsExpired(isCredentialExpired(customer))
                                    .build())
                    .orElseThrow(()-> new UsernameNotFoundException(username));
            }else{
            return userRepository.findByPhone(username)

                    .map(customer ->
                            User.withUsername(username)
                                    .authorities(Role.USER.name())
                                    .password(customer.getPassword())
                                    .accountExpired(isExpired(customer))
                                    .accountLocked(customer.isLocked())
                                    .credentialsExpired(isCredentialExpired(customer))
                                    .build())
                    .orElseThrow(()-> new UsernameNotFoundException(username));
        }
    }
    private boolean isCredentialExpired(com.edusn.Digizenger.Demo.auth.entity.User customer){
        if (null !=customer.getValidPassDate()){
            LocalDateTime validPassDate = customer.getValidPassDate();
            if (validPassDate.isBefore(LocalDateTime.now())){
                return true;
            }
        }
        return false;
    }

    private boolean  isExpired(com.edusn.Digizenger.Demo.auth.entity.User customer){
        if (null != customer.getRetiredDate()){
            LocalDateTime retiredDate = customer.getRetiredDate();
            if (retiredDate.isBefore(LocalDateTime.now())){
                return true;
            }
        }
        return false;
    }
}
