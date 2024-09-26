package com.edusn.Digizenger.Demo.service.post;

import com.edusn.Digizenger.Demo.dto.response.Response;
import com.edusn.Digizenger.Demo.dto.response.home.post.PostDto;
import com.edusn.Digizenger.Demo.entity.auth.User;
import com.edusn.Digizenger.Demo.entity.post.Post;
import org.springframework.http.ResponseEntity;


public interface PostService  {
    ResponseEntity<PostDto> upload(Post post,User user);

    ResponseEntity<PostDto> updatePost(Post post, User user);

    ResponseEntity<?> deletePost(long id);

    ResponseEntity<Response> getPostByPage(int _page, int _limit);
}
