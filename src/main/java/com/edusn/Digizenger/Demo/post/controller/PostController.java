package com.edusn.Digizenger.Demo.post.controller;

import com.edusn.Digizenger.Demo.auth.dto.response.Response;
import com.edusn.Digizenger.Demo.post.dto.PostDto;
import com.edusn.Digizenger.Demo.auth.entity.User;
import com.edusn.Digizenger.Demo.post.entity.Post;
import com.edusn.Digizenger.Demo.security.JWTService;
import com.edusn.Digizenger.Demo.post.service.PostService;
import com.edusn.Digizenger.Demo.utilis.CheckEmailOrPhoneUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/posts")
public class PostController {
    @Autowired
    private PostService postService;
    @Autowired
    private JWTService jwtService;
    @Autowired
    private CheckEmailOrPhoneUtil checkEmailOrPhoneUtil;

    @PostMapping("/upload")
    public ResponseEntity<Response> upload(@RequestParam("description") String description
            , @RequestParam("postType") Post.PostType postType
            , @RequestParam("file") MultipartFile multipartFile
            , HttpServletRequest request) throws IOException {

        String token = jwtService.getJWTFromRequest(request);
        String emailOrPhone = jwtService.extractUsername(token);
       User user= checkEmailOrPhoneUtil.checkEmailOrPhone(emailOrPhone);

        return postService.upload(description,postType,user,multipartFile);
    }


    @PutMapping("/update-post/{id}")
    public ResponseEntity<Response> updatePost(@PathVariable("id") Long id
            ,@RequestParam("description") String discription
            , @RequestParam("postType") Post.PostType postType
            , @RequestParam("file") MultipartFile multipartFile
            ,HttpServletRequest request, String imageName) throws IOException {

        String token = jwtService.getJWTFromRequest(request);
        String emailOrPhone = jwtService.extractUsername(token);
        User user= checkEmailOrPhoneUtil.checkEmailOrPhone(emailOrPhone);
        return  postService.updatePost(id,discription,postType,user,multipartFile,imageName);
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deletePost(@PathVariable("id") long id) {

        return postService.deletePost(id);
    }
    @GetMapping("/getPost")
    public ResponseEntity<Response> getPosts(
            @RequestParam(defaultValue = "1") int _page,
            @RequestParam(defaultValue = "10") int _limit) {
        return postService.getPostByPage(_page,_limit);
    }
    @GetMapping("/image")
    public ResponseEntity<Response> getImage(@RequestParam("imageName") String imageName) throws IOException {
        return postService.getImage(imageName);
    }
}
