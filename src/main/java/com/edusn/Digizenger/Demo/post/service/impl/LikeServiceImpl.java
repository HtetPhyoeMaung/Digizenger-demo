package com.edusn.Digizenger.Demo.post.service.impl;

import com.edusn.Digizenger.Demo.auth.dto.response.Response;
import com.edusn.Digizenger.Demo.auth.entity.User;
import com.edusn.Digizenger.Demo.exception.CustomNotFoundException;
import com.edusn.Digizenger.Demo.notification.entity.Notification;
import com.edusn.Digizenger.Demo.notification.service.NotificationService;
import com.edusn.Digizenger.Demo.post.dto.LikeDto;
import com.edusn.Digizenger.Demo.post.entity.Like;
import com.edusn.Digizenger.Demo.post.entity.Post;
import com.edusn.Digizenger.Demo.post.repo.LikeRepository;
import com.edusn.Digizenger.Demo.post.repo.PostRepository;
import com.edusn.Digizenger.Demo.post.service.LikeService;
import com.edusn.Digizenger.Demo.storage.StorageService;
import jakarta.persistence.Access;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class LikeServiceImpl implements LikeService {
    @Autowired
    private PostRepository postRepository;

    @Autowired
    private LikeRepository likeRepository;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private StorageService storageService;

    @Override
    public ResponseEntity<Response> isLike(Long id, User user) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new CustomNotFoundException("Post not found by " + id));

        Optional<Like> alreadyLike = likeRepository.findFirstByPostAndUser(post, user);
        Response response;

        if (alreadyLike.isPresent() && alreadyLike.get().isLiked()) {
            // If the post is already liked, we unlike it
            alreadyLike.get().setLiked(false);
            Like like=  likeRepository.save(alreadyLike.get());
            LikeDto likeDto=LikeDto.builder()
                    .createdDate(alreadyLike.get().getCreatedDate())
                    .isLike(like.isLiked())
                    .build();
            response = Response.builder()
                    .statusCode(HttpStatus.FOUND.value())
                    .likeDto(likeDto)
                    .message("User unliked Post " + post.getId())
                    .build();
        } else if (alreadyLike.isPresent() && !alreadyLike.get().isLiked()) {
            alreadyLike.get().setLiked(true);
            Like like=   likeRepository.save(alreadyLike.get());
            if(like.isLiked() && !post.getUser().equals(like.getUser())){
                Notification notification=Notification.builder()
                        .createDate(LocalDateTime.now())
                        .isRead(false)
                        .user(post.getUser())
                        .post(post)
                        .profile(user.getProfile())
                        .message(like.getUser().getFirstName()+" "+like.getUser().getLastName() +" loved your post!")
                        .build();

                notificationService.sendNotiMessage(notification);
            }
            LikeDto likeDto=LikeDto.builder()
                    .isLike(like.isLiked())
                    .createdDate(alreadyLike.get().getCreatedDate())
                    .modifiedDate(LocalDateTime.now())
                    .build();
            response = Response.builder()
                    .statusCode(HttpStatus.OK.value())
                    .likeDto(likeDto)
                    .message("User liked Post " + post.getId())  // Correct message to "liked"
                    .build();
        } else {
            // If no record exists, this is the first time the user is liking the post
            Like like= likeRepository.save(Like.builder()
                            .post(post)
                            .createdDate(LocalDateTime.now())
                            .isLiked(true)
                            .user(user)
                            .build());
            if(like.isLiked() && !post.getUser().equals(like.getUser())){
                Notification notification=Notification.builder()
                        .createDate(LocalDateTime.now())
                        .isRead(false)
                        .user(post.getUser())
                        .post(post)
                        .profile(user.getProfile())
                        .message(like.getUser().getProfile().getUsername()+"liked your post!")
                        .build();

                notificationService.sendNotiMessage(notification);
            }
            LikeDto likeDto=LikeDto.builder()
                    .isLike(like.isLiked())
                    .createdDate(like.getCreatedDate())
                    .build();
            response = Response.builder()
                    .statusCode(HttpStatus.OK.value())
                    .likeDto(likeDto)
                    .message("User first time liked Post " + post.getId())
                    .build();
        }

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
