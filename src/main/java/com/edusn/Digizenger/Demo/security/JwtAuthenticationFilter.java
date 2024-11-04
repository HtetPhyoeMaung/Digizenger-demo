package com.edusn.Digizenger.Demo.security;


import com.edusn.Digizenger.Demo.auth.entity.Role;
import com.edusn.Digizenger.Demo.auth.entity.User;
import com.edusn.Digizenger.Demo.auth.repo.UserRepository;
import com.edusn.Digizenger.Demo.exception.CustomNotFoundException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;


public class JwtAuthenticationFilter extends OncePerRequestFilter {
    @Autowired
    private JWTService jwtService;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserDetailServiceForUser userDetailServiceForUser;

    @Autowired
    private UserDetailServiceForAdmin userDetailServiceForAdmin;

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
           User user = userRepository.findByEmail(email).orElseThrow(
                   ()-> new CustomNotFoundException("User not found by "+email));
           if (user.getRole().equals(Role.USER.name())){
               UserDetails userDetails = userDetailServiceForUser.loadUserByUsername(email);
               SecurityContext context = SecurityContextHolder.createEmptyContext();
               UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
               token.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
               context.setAuthentication(token);
               SecurityContextHolder.setContext(context);
           }else if (user.getRole().equals(Role.ADMIN.name())){
               UserDetails userDetails = userDetailServiceForAdmin.loadUserByUsername(email);
               SecurityContext context = SecurityContextHolder.createEmptyContext();
               UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
               token.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
               context.setAuthentication(token);
               SecurityContextHolder.setContext(context);
           }

            }
        filterChain.doFilter(request,response);

        }


    }





