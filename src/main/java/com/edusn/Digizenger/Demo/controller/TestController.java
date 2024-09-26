package com.edusn.Digizenger.Demo.controller;


import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/digizenger/api/")
public class TestController {



    @GetMapping("/test")
    public String test(){
        return "Test Success";
    }



}
