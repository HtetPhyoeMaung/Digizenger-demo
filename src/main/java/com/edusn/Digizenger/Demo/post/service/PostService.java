package com.edusn.Digizenger.Demo.post.service;

import com.edusn.Digizenger.Demo.auth.dto.response.Response;
import com.edusn.Digizenger.Demo.post.dto.PostDto;
import com.edusn.Digizenger.Demo.auth.entity.User;
import com.edusn.Digizenger.Demo.post.entity.Post;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


public interface PostService  {
    ResponseEntity<Response> upload(String description, Post.PostType postType, User user, MultipartFile multipartFile) throws IOException;

    ResponseEntity<Response> updatePost(Long id,String description, Post.PostType postType, User user, MultipartFile multipartFile, String imageName) throws IOException;

    ResponseEntity<?> deletePost(long id);

    ResponseEntity<Response> getPostByPage(int _page, int _limit);

    ResponseEntity<Response> increaseView(Long id,User user);

    ResponseEntity<Response> isLike(Long id, User user);
}
