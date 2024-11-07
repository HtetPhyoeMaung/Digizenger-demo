package com.edusn.Digizenger.Demo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.CacheControl;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
      registry.addMapping("/**")
              .allowedOrigins("http://188.166.224.85","http://localhost:5173","http://localhost:3000","https://digizenger.info","http://localhost:8081","http://127.0.0.1:5500")
              .allowedMethods("GET", "POST", "PUT", "DELETE")
              .allowedHeaders("*")
              .allowCredentials(true);
    }

}
