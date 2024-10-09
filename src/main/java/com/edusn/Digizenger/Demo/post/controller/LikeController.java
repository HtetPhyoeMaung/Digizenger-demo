package com.edusn.Digizenger.Demo.post.controller;

import com.edusn.Digizenger.Demo.auth.dto.response.Response;
import com.edusn.Digizenger.Demo.auth.entity.User;
import com.edusn.Digizenger.Demo.post.service.LikeService;
import com.edusn.Digizenger.Demo.utilis.GetUserByRequest;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/posts")
@CrossOrigin
public class LikeController {
    @Autowired
    private GetUserByRequest getUserByRequest;

    @Autowired
    private LikeService likeService;

    @PostMapping("/isLike/{postId}")
    public ResponseEntity<Response> isLike(@PathVariable("postId") Long id, HttpServletRequest request) {
        User user= getUserByRequest.getUser(request);
        return likeService.isLike(id,user);
    }
}
