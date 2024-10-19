package com.edusn.Digizenger.Demo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
      registry.addMapping("/**")
              .allowedOrigins("http://128.199.144.3:5173","http://localhost:5173","http://localhost:3000")
              .allowedMethods("GET", "POST", "PUT", "DELETE")
              .allowedHeaders("*")
              .allowCredentials(true);
    }
}
