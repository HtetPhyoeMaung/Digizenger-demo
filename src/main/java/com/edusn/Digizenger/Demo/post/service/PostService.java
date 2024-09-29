package com.edusn.Digizenger.Demo.post.service;

import com.edusn.Digizenger.Demo.auth.dto.response.Response;
import com.edusn.Digizenger.Demo.post.dto.PostDto;
import com.edusn.Digizenger.Demo.auth.entity.User;
import com.edusn.Digizenger.Demo.post.entity.Post;
import org.springframework.http.ResponseEntity;


public interface PostService  {
    ResponseEntity<PostDto> upload(Post post,User user);

    ResponseEntity<PostDto> updatePost(Post post, User user);

    ResponseEntity<?> deletePost(long id);

    ResponseEntity<Response> getPostByPage(int _page, int _limit);
}
