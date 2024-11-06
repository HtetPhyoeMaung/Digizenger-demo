package com.edusn.Digizenger.Demo.security;


import com.edusn.Digizenger.Demo.auth.entity.Role;
import com.edusn.Digizenger.Demo.auth.entity.User;
import com.edusn.Digizenger.Demo.auth.repo.UserRepository;
import com.edusn.Digizenger.Demo.exception.CustomNotFoundException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    @Autowired
    private JWTService jwtService;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserDetailServiceForUser userDetailsService;


    public JwtAuthenticationFilter(){

    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {


        String authHeader = request.getHeader("Authorization");
        String jwtToken;
        String email;

        if(authHeader == null || authHeader.isBlank()){
            filterChain.doFilter(request,response);
            return;
        }

        jwtToken = authHeader.substring(7);
        email = jwtService.extractUsername(jwtToken);

        if(email != null && SecurityContextHolder.getContext().getAuthentication() == null){
            log.info("Reach State 1");
           User user = userRepository.findByEmail(email).orElseThrow(
                   ()-> new CustomNotFoundException("User not found by "+email));
           log.info("Reach State 2");
           if (user.getRole().equals(Role.USER.name())){
               UserDetails userDetails = userDetailsService.loadUserByUsername(email);
               SecurityContext context = SecurityContextHolder.createEmptyContext();
               UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
               token.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
               context.setAuthentication(token);
               SecurityContextHolder.setContext(context);
             log.info("Reach State 3");
           }
           log.info("Reach State 4");

            }
        filterChain.doFilter(request,response);

        }


    }





