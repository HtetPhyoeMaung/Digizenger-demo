package com.edusn.Digizenger.Demo.post.controller;

import com.edusn.Digizenger.Demo.auth.dto.response.Response;
import com.edusn.Digizenger.Demo.post.dto.PostDto;
import com.edusn.Digizenger.Demo.auth.entity.User;
import com.edusn.Digizenger.Demo.post.entity.Post;
import com.edusn.Digizenger.Demo.security.JWTService;
import com.edusn.Digizenger.Demo.post.service.PostService;
import com.edusn.Digizenger.Demo.utilis.CheckEmailOrPhoneUtil;
import com.edusn.Digizenger.Demo.utilis.GetUserByRequest;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@CrossOrigin
@RequestMapping("/api/v1/posts")
public class PostController {
    @Autowired
    private PostService postService;
    @Autowired
    private JWTService jwtService;
    @Autowired
    private CheckEmailOrPhoneUtil checkEmailOrPhoneUtil;
    @Autowired
    private GetUserByRequest getUserByRequest;

    @PostMapping("/upload")
    public ResponseEntity<Response> upload(@RequestParam("description") String description
            , @RequestParam("postType") Post.PostType postType
            , @RequestParam(value = "file",required = false) MultipartFile multipartFile
            , HttpServletRequest request) throws IOException {

        User user= getUserByRequest.getUser(request);
        if(multipartFile==null || multipartFile.isEmpty()){
              return postService.upload(description,postType,user,null);

        }
        return postService.upload(description,postType,user,multipartFile);
    }


    @PutMapping("/update-post/{id}")
    public ResponseEntity<Response> updatePost(@PathVariable("id") Long id
            ,@RequestParam("description") String discription
            , @RequestParam("postType") Post.PostType postType
            , @RequestParam("file") MultipartFile multipartFile
            ,HttpServletRequest request, String imageName) throws IOException {


        User user= getUserByRequest.getUser(request);
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

    @PostMapping("/increase-view/{postId}")
    public ResponseEntity<Response>  increaseView(@PathVariable("postId") Long id, HttpServletRequest request) {
        User user= getUserByRequest.getUser(request);
        System.out.print("View");
        return postService.increaseView(id,user);
    }
    @PostMapping("/isLike/{postId}")
    public ResponseEntity<Response>  isLike(@PathVariable("postId") Long id, HttpServletRequest request) {
        User user= getUserByRequest.getUser(request);
        return postService.isLike(id,user);
    }

}
