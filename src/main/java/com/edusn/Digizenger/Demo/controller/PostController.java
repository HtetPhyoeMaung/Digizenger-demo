package com.edusn.Digizenger.Demo.controller;

import com.edusn.Digizenger.Demo.dto.response.Response;
import com.edusn.Digizenger.Demo.dto.response.home.post.PostDto;
import com.edusn.Digizenger.Demo.entity.auth.User;
import com.edusn.Digizenger.Demo.entity.post.Post;
import com.edusn.Digizenger.Demo.security.JWTService;
import com.edusn.Digizenger.Demo.service.post.PostService;
import com.edusn.Digizenger.Demo.utilis.CheckEmailOrPhoneUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/digizenger/api/v1/posts")
public class PostController {
    @Autowired
    private PostService postService;
    @Autowired
    private JWTService jwtService;
    @Autowired
    private CheckEmailOrPhoneUtil checkEmailOrPhoneUtil;

    @PostMapping("/upload")
    public ResponseEntity<PostDto> upload(@RequestBody Post post, HttpServletRequest request)
    {
        String token = jwtService.getJWTFromRequest(request);
        String emailOrPhone = jwtService.extractUsername(token);
       User user= checkEmailOrPhoneUtil.checkEmailOrPhone(emailOrPhone);

        return postService.upload(post,user);
    }
    @PutMapping("/update-post")
    public ResponseEntity<PostDto> updatePost(@RequestBody Post post,HttpServletRequest request){
        String token = jwtService.getJWTFromRequest(request);
        String emailOrPhone = jwtService.extractUsername(token);
        User user= checkEmailOrPhoneUtil.checkEmailOrPhone(emailOrPhone);
        return  postService.updatePost(post,user);
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteContent(@PathVariable("id") long id) {

        return postService.deletePost(id);
    }
    @GetMapping("/getPost")
    public ResponseEntity<Response> getPosts(
            @RequestParam(defaultValue = "1") int _page,
            @RequestParam(defaultValue = "10") int _limit) {
        return postService.getPostByPage(_page,_limit);
    }
}
