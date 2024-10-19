package com.edusn.Digizenger.Demo.notification.service;

import com.edusn.Digizenger.Demo.auth.dto.response.Response;
import com.edusn.Digizenger.Demo.auth.entity.User;
import com.edusn.Digizenger.Demo.notification.entity.Notification;
import org.springframework.http.ResponseEntity;

public interface NotificationService {
    void sendNotiMessage(Notification notification);

    ResponseEntity<Response> getAllNotification(User user);

    ResponseEntity<Response> deleteNotification(Long id);

    ResponseEntity<Response> markAllAsRead(User user);
}
