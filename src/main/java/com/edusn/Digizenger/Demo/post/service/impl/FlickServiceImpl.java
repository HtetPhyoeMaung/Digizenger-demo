package com.edusn.Digizenger.Demo.post.service.impl;

import com.amazonaws.services.kms.model.AlreadyExistsException;
import com.edusn.Digizenger.Demo.auth.dto.response.Response;
import com.edusn.Digizenger.Demo.auth.entity.User;
import com.edusn.Digizenger.Demo.exception.CustomNotFoundException;
import com.edusn.Digizenger.Demo.notification.dto.NotificationDto;
import com.edusn.Digizenger.Demo.notification.entity.Notification;
import com.edusn.Digizenger.Demo.notification.repo.NotificationRepository;
import com.edusn.Digizenger.Demo.post.entity.Flick;
import com.edusn.Digizenger.Demo.post.entity.Post;
import com.edusn.Digizenger.Demo.post.repo.FlickRepository;
import com.edusn.Digizenger.Demo.post.repo.PostRepository;
import com.edusn.Digizenger.Demo.post.service.FlickService;
import com.edusn.Digizenger.Demo.utilis.DateUtil;
import com.edusn.Digizenger.Demo.utilis.GeneratePostUrl;
import com.edusn.Digizenger.Demo.utilis.GetUserByRequest;
import com.edusn.Digizenger.Demo.utilis.MapperUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Transactional
public class FlickServiceImpl implements FlickService {
    @Autowired
    private FlickRepository flickRepository;
    @Autowired
    private PostRepository postRepository;

    @Autowired
    private GetUserByRequest getUserByRequest;
    @Autowired
    private GeneratePostUrl generatePostUrl;
    @Autowired
    private DateUtil dateUtil;
    @Autowired
    private NotificationRepository notificationRepository;
    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    @Autowired
    private MapperUtil mapperUtil;

    @Override
    public ResponseEntity<Response> flick(int postId, HttpServletRequest request) {
        Post existPost = postRepository.findById((long) postId)
                .orElseThrow(() -> new CustomNotFoundException("Post not found by PostId :" + postId));

        User flickUser = getUserByRequest.getUser(request);

        // Check if flick already exists and handle it
        if (flickRepository.findByPostAndUser(existPost, flickUser).isPresent()) {
            throw new AlreadyExistsException("User : " + flickUser.getProfile().getUsername() +
                    " was already flicked to Post : " + postId );
        }

        // Create and save new Flick
        Flick flick = new Flick();
        flick.setUser(flickUser);
        flick.setFlickedPostLinkUrl(existPost.getPostLinkUrl());
        flick.setPost(existPost);
        flickRepository.save(flick);

        String message = flickUser.getProfile().getUsername() + " flicked " +
                "on " + existPost.getUser().getProfile().getUsername() + "'s post.";
        Notification notificationForPostOwner = Notification.builder()
                .profile(flickUser.getProfile())
                .message(message)
                .user(existPost.getUser())
                .post(existPost)
                .type(Notification.Type.FLICK)
                .createDate(LocalDateTime.now())
                .build();

        notificationRepository.save(notificationForPostOwner);

        NotificationDto notificationDtoForPostOwner = NotificationDto.builder()
                .id(notificationForPostOwner.getId())
                .message(notificationForPostOwner.getMessage())
                .postDto(mapperUtil.convertToPostDto(notificationForPostOwner.getPost()))
                .profileDto(mapperUtil.convertToProfileDto(notificationForPostOwner.getProfile()))
                .createDate(dateUtil.formattedDate(notificationForPostOwner.getCreateDate()))
                .type(notificationForPostOwner.getType())
                .build();

        messagingTemplate.convertAndSendToUser(String.valueOf(existPost.getUser().getId()),"/queue/private-notification",notificationDtoForPostOwner);

        // Create a new notification for neighbors
        Notification notificationForNeighbor = Notification.builder()
                .message(message)
                .post(existPost)
                .profile(flickUser.getProfile())
                .type(Notification.Type.FLICK)
                .createDate(LocalDateTime.now())
                .build();

        // Set up the notification DTO
        NotificationDto notificationDtoForNeighbors = NotificationDto.builder()
                .message(message)
                .postDto(mapperUtil.convertToPostDto(notificationForNeighbor.getPost()))
                .isRead(notificationForNeighbor.isRead())
                .type(notificationForNeighbor.getType())
                .createDate(dateUtil.formattedDate(notificationForNeighbor.getCreateDate()))
                .build();

        // Notify all neighbors
        flickUser.getProfile().getNeighbors().forEach(neighbors -> {
            notificationForNeighbor.setUser(neighbors.getUser());
            notificationRepository.save(notificationForNeighbor);  // Save once for each neighbor
            notificationDtoForNeighbors.setId(notificationForNeighbor.getId());
            messagingTemplate.convertAndSendToUser(String.valueOf(neighbors.getId()),
                    "/queue/private-notification", notificationDtoForNeighbors);
        });

        // Prepare response
        Response response = Response.builder()
                .statusCode(HttpStatus.CREATED.value())
                .message(existPost.getId() + " was flicked successfully by User : " +
                        flickUser.getProfile().getUsername() + ".")
                .build();

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }


}
