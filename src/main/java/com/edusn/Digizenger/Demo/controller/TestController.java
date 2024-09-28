package com.edusn.Digizenger.Demo.controller;

import com.edusn.Digizenger.Demo.dto.request.LoginRequest;
import com.edusn.Digizenger.Demo.dto.request.RegisterRequest;
import com.edusn.Digizenger.Demo.dto.response.Response;
import com.edusn.Digizenger.Demo.service.auth.*;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/digizenger/api/test")
public class TestController {
   @GetMapping("/serverTest")
    public ResponseEntity<Response> getServerTest()  {
       Response response = Response.builder()
               .statusCode(HttpStatus.OK.value())
               .message("Test Success")
               .build();
       return new ResponseEntity<>(response, HttpStatus.OK);
   }

}
