

package com.edusn.Digizenger.Demo.config;

import com.edusn.Digizenger.Demo.auth.entity.Role;
import com.edusn.Digizenger.Demo.security.JwtAuthEntryPoint;
import com.edusn.Digizenger.Demo.security.JwtAuthenticationFilter;
import com.edusn.Digizenger.Demo.security.UserDetailServiceForUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    //hello
    @Autowired
    private UserDetailServiceForUser userDetailsService;


    @Autowired
    private JwtAuthEntryPoint jwtAuthEntryPoint;
    @Autowired
    private UserDetailServiceForUser userDetailServiceForUser;



    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity)throws Exception{
       httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(request -> request.requestMatchers("/api/v1/auth/**","/api/v1/test/**",

                                        "/v2/api-docs",
                                        "/v3/api-docs",
                                        "/v3/api-docs/**",
                                        "/swagger-resources",
                                        "/swagger-resources/**",
                                        "/configuration/ui",
                                        "/configuration/security",
                                        "/swagger-ui/**",
                                        "/wabjars/**",
                                        "/swagger-ui.html")
                        .permitAll()



                                .requestMatchers("/api/v1/admin/**").hasAuthority(Role.ADMIN.name())
                                .requestMatchers("/api/v1/posts/**").hasAnyAuthority(Role.USER.name(),Role.ADMIN.name(),Role.SUPER.name())
                                .requestMatchers("/api/v1/profile/**").hasAnyAuthority(Role.USER.name(),Role.ADMIN.name(),Role.SUPER.name())
                        .requestMatchers("/api/v1/user/**").hasAnyAuthority(Role.USER.name(),Role.ADMIN.name(),Role.SUPER.name())
                                .requestMatchers("/api/v1/super/**").hasAnyAuthority(Role.SUPER.name(),Role.ADMIN.name())
                        .anyRequest().authenticated()
                        )
               .exceptionHandling(handler->handler.authenticationEntryPoint(jwtAuthEntryPoint))
               .sessionManagement(manager->manager.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
       httpSecurity.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
       return httpSecurity.build();



    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter(){
        return new JwtAuthenticationFilter();
    }


    @Bean
    AuthenticationManager configure(HttpSecurity http,PasswordEncoder passwordEncoder) throws Exception {
        var builder =http.getSharedObject(AuthenticationManagerBuilder.class);
        builder.authenticationProvider(getUserProvider(passwordEncoder));

//        builder.authenticationProvider(getAdminProvider(passwordEncoder));
//
//        builder.authenticationProvider(getSuperProvider(passwordEncoder));
        return builder.build();
    }



    private AuthenticationProvider getUserProvider(PasswordEncoder passwordEncoder){
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder);
        provider.setUserDetailsService(userDetailServiceForUser);
        return provider;
    }

//    private AuthenticationProvider getAdminProvider(PasswordEncoder passwordEncoder){
//
//        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(passwordEncoder);
//        provider.setUserDetailsService(userDetailServiceForAdmin);
//        return provider;
//    }
//    private AuthenticationProvider getSuperProvider(PasswordEncoder passwordEncoder){
//
//        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(passwordEncoder);
//        provider.setUserDetailsService(userDetailServiceForSuper);
//        return provider;
//    }



}
