package com.edusn.Digizenger.Demo.profile.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/digizenger/api/v1/profile")
public class ProfileController {

    @GetMapping("/test")
    public String test(){
        return "successfully test";
    }
}
