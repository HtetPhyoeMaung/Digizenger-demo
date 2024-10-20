package com.edusn.Digizenger.Demo.notification.service.impl;

import com.edusn.Digizenger.Demo.auth.dto.response.Response;
import com.edusn.Digizenger.Demo.auth.entity.User;
import com.edusn.Digizenger.Demo.exception.CustomNotFoundException;
import com.edusn.Digizenger.Demo.notification.dto.NotificationDto;
import com.edusn.Digizenger.Demo.notification.entity.Notification;
import com.edusn.Digizenger.Demo.notification.repo.NotificationRepository;
import com.edusn.Digizenger.Demo.notification.service.NotificationService;
import com.edusn.Digizenger.Demo.post.entity.Post;
import com.edusn.Digizenger.Demo.utilis.MapperUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class NotificationServiceImpl implements NotificationService {
    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    @Override
    public void sendNotiMessage(Notification notification) {
     Notification saveNotification=   notificationRepository.save(notification);
        NotificationDto notificationDto=NotificationDto.builder()
                .message(saveNotification.getMessage())
                .id(saveNotification.getId())
                .createDate(saveNotification.getCreateDate())
                .postId(saveNotification.getPost().getId())
                .isRead(saveNotification.isRead())
                .userId(saveNotification.getUser().getId())
                .build();
        if(notification.getProfileUrl()!=null){
            notificationDto.setProfileUrl(notification.getProfileUrl());
        }
        messagingTemplate.convertAndSendToUser(String.valueOf(notificationDto.getUserId()),"/queue/private-notification",notificationDto);

    }

    @Override
    public ResponseEntity<Response> getAllNotification(User user) {
        List<Notification> notificationList=notificationRepository.findByUser(user);
        List<NotificationDto> notificationDtos = notificationList.stream()
                .map(MapperUtil::convertToNotificationDto).toList();
        long unreadCount = notificationList.stream()
                .filter(notification -> !notification.isRead()) // Filter unread notifications
                .count();

        Response response=Response.builder()
                .notificationDtoList(notificationDtos)
                .unreadNotificationCount(unreadCount)
                .statusCode(HttpStatus.OK.value())
                .build();
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Response> deleteNotification(Long id) {
        Notification notification=notificationRepository.findById(id).orElseThrow(()->new CustomNotFoundException("Notification not found by this "+id));
        notificationRepository.delete(notification);
        return  new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Override
    public ResponseEntity<Response> markAllAsRead(User user) {
        List<Notification> notificationList = notificationRepository.findByUserAndIsReadFalse(user);
        notificationList.forEach(notification -> notification.setRead(true));
        notificationRepository.saveAll(notificationList);
        List<NotificationDto> notificationDto = notificationList.stream()
                .map(MapperUtil::convertToNotificationDto).toList();
        Response response=Response.builder()
                .notificationDtoList(notificationDto)
                .unreadNotificationCount(0L)
                .statusCode(HttpStatus.OK.value())
                .build();

        return new ResponseEntity<>(response,HttpStatus.OK);
    }
}
