package com.edusn.Digizenger.Demo.post.service.impl;

import com.amazonaws.services.kms.model.AlreadyExistsException;
import com.edusn.Digizenger.Demo.auth.dto.response.Response;
import com.edusn.Digizenger.Demo.auth.entity.User;
import com.edusn.Digizenger.Demo.exception.CustomNotFoundException;
import com.edusn.Digizenger.Demo.post.entity.Flick;
import com.edusn.Digizenger.Demo.post.entity.Post;
import com.edusn.Digizenger.Demo.post.repo.FlickRepository;
import com.edusn.Digizenger.Demo.post.repo.PostRepository;
import com.edusn.Digizenger.Demo.post.service.FlickService;
import com.edusn.Digizenger.Demo.utilis.GeneratePostUrl;
import com.edusn.Digizenger.Demo.utilis.GetUserByRequest;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class FlickServiceImpl implements FlickService {
    @Autowired
    private FlickRepository flickRepository;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private GetUserByRequest getUserByRequest;
    @Autowired
    private GeneratePostUrl generatePostUrl;
    @Override
    public ResponseEntity<Response> flick(int postId, HttpServletRequest request) {
       Post existPost =  postRepository.findById((long)postId).orElseThrow(
               ()-> new CustomNotFoundException("Post not found by PostId :"+postId)
       );
        User flickUser = getUserByRequest.getUser(request);

       if (flickRepository.findByPostAndUser(existPost,flickUser).isPresent()) {
           throw new  AlreadyExistsException("User : " + flickUser.getProfile().getUser() + " was already flicked to Post : "+postId+".");
       }
        Flick flick = new Flick();
        flick.setUser(flickUser);
        flick.setFlickedPostLinkUrl(existPost.getPostLinkUrl());
        flick.setPost(existPost);
        flickRepository.save(flick);

        Response response = Response.builder()
                .statusCode(HttpStatus.CREATED.value())
                .message(existPost.getId()+" was flicked Successfully by User : "+flickUser.getProfile().getUsername()+".")
                .build();

        return new ResponseEntity<>(response,HttpStatus.CREATED);
    }

}
