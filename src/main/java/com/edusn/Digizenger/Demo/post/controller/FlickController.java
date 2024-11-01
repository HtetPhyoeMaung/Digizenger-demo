package com.edusn.Digizenger.Demo.post.controller;

import com.edusn.Digizenger.Demo.auth.dto.response.Response;
import com.edusn.Digizenger.Demo.post.service.FlickService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/posts")
@CrossOrigin
public class FlickController {

    @Autowired
    private FlickService flickService;

    @PostMapping("/flick/{postId}")
    public ResponseEntity<Response> flick(@PathVariable("postId") int postId, HttpServletRequest request){
       return flickService.flick(postId,request);

    }
}
