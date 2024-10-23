package com.edusn.Digizenger.Demo.notification.service;

import com.edusn.Digizenger.Demo.auth.dto.response.Response;
import com.edusn.Digizenger.Demo.auth.entity.User;
import com.edusn.Digizenger.Demo.notification.entity.Notification;
import org.springframework.http.ResponseEntity;
import com.edusn.Digizenger.Demo.profile.entity.Profile;

public interface NotificationService {
    void sendNotiMessage(Notification notification);

    ResponseEntity<Response> getAllNotification(User user,int page,int size);

    ResponseEntity<Response> deleteNotification(Long id);

    ResponseEntity<Response> markAllAsRead(User user);

    void sendFollowNotification(Profile profile, Profile toFollowUserProfile);

}
