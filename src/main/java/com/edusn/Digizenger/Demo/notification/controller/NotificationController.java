package com.edusn.Digizenger.Demo.notification.controller;

import com.edusn.Digizenger.Demo.auth.dto.request.RegisterRequest;
import com.edusn.Digizenger.Demo.auth.dto.response.Response;
import com.edusn.Digizenger.Demo.auth.entity.User;
import com.edusn.Digizenger.Demo.notification.entity.Notification;
import com.edusn.Digizenger.Demo.notification.service.NotificationService;
import com.edusn.Digizenger.Demo.utilis.GetUserByRequest;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/notification")
public class NotificationController {
    @Autowired
    private  NotificationService notificationService;
    @Autowired
    private GetUserByRequest getUserByRequest;
    @GetMapping
    public ResponseEntity<Response> getAllNotification(HttpServletRequest request,@RequestParam(defaultValue = "0") int page,
                                                       @RequestParam(defaultValue = "10") int size) throws MessagingException {
        User user=getUserByRequest.getUser(request);
        return notificationService.getAllNotification(user,page, size);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Response> deleteNotification(@PathVariable("id") Long id) throws MessagingException {
        return notificationService.deleteNotification(id);
    }
    @PostMapping
    public ResponseEntity<Response> resetNotificationCount(HttpServletRequest request) {
        User user=getUserByRequest.getUser(request);

       return notificationService.markAllAsRead(user);
    }
}
