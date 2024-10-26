package com.edusn.Digizenger.Demo.notification.service.impl;

import com.edusn.Digizenger.Demo.auth.dto.response.Response;
import com.edusn.Digizenger.Demo.auth.entity.User;
import com.edusn.Digizenger.Demo.auth.repo.UserRepository;
import com.edusn.Digizenger.Demo.exception.CustomNotFoundException;
import com.edusn.Digizenger.Demo.notification.dto.NotificationDto;
import com.edusn.Digizenger.Demo.notification.entity.Notification;
import com.edusn.Digizenger.Demo.notification.repo.NotificationRepository;
import com.edusn.Digizenger.Demo.notification.service.NotificationService;
import com.edusn.Digizenger.Demo.profile.dto.response.myProfile.ProfileDto;
import com.edusn.Digizenger.Demo.profile.entity.Profile;
import com.edusn.Digizenger.Demo.storage.StorageService;
import com.edusn.Digizenger.Demo.utilis.MapperUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class NotificationServiceImpl implements NotificationService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private StorageService storageService;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Override
    public void sendNotiMessage(Notification notification) {
     Notification saveNotification=   notificationRepository.save(notification);
        ProfileDto profileDto=MapperUtil.convertToProfileDto(saveNotification.getProfile());
        if(saveNotification.getProfile().getProfileImageName()!=null){
            profileDto.setProfileImageUrl(storageService.getImageByName(saveNotification.getProfile().getProfileImageName()));
        }
        NotificationDto notificationDto=NotificationDto.builder()
                .message(saveNotification.getMessage())
                .id(saveNotification.getId())
                .createDate(saveNotification.getCreateDate())
                .postId(saveNotification.getPost()!=null?saveNotification.getPost().getId():null)
                .isRead(saveNotification.isRead())
                .userId(saveNotification.getUser().getId())
                .profileDto(profileDto)
                .build();

        messagingTemplate.convertAndSendToUser(String.valueOf(notificationDto.getUserId()),"/queue/private-notification",notificationDto);

    }

    @Override
    public ResponseEntity<Response> getAllNotification(User user, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createDate"));
        Page<Notification> notificationPage = notificationRepository.findByUser(user,pageable);
        List<NotificationDto> notificationDtos = notificationPage.getContent().stream()
                .map(MapperUtil::convertToNotificationDto)
                .toList();
        long unreadCount = notificationPage.getContent().stream()
                .filter(notification -> !notification.isRead())
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

    @Override
    public void sendFollowNotification(Profile profile, Profile toFollowUserProfile) {
        if(!toFollowUserProfile.getFollowing().contains(profile)){
            Notification notification=Notification.builder()
                    .createDate(LocalDateTime.now())
                    .isRead(false)
                    .user(toFollowUserProfile.getUser())
                    .message(profile.getUser().getFirstName()+" "+profile.getUser().getLastName()+ " started following you!")
                    .profile(profile)
                    .build();

            sendNotiMessage(notification);

        }else{
            Notification notification=Notification.builder()
                    .createDate(LocalDateTime.now())
                    .isRead(false)
                    .user(toFollowUserProfile.getUser())
                    .message(profile.getUser().getFirstName()+" "+profile.getUser().getLastName()+ "  following back!")
                    .profile(profile)
                    .build();

            sendNotiMessage(notification);

            Notification notificationNeighbors1=Notification.builder()
                    .createDate(LocalDateTime.now())
                    .isRead(false)
                    .user(toFollowUserProfile.getUser())
                    .message(profile.getUser().getFirstName()+" "+profile.getUser().getLastName()+ " is neighbor now!")
                    .profile(profile)
                    .build();

            sendNotiMessage(notificationNeighbors1);

            Notification notificationNeighbors2=Notification.builder()
                    .createDate(LocalDateTime.now())
                    .isRead(false)
                    .user(profile.getUser())
                    .message(toFollowUserProfile.getUser().getFirstName()+" "+toFollowUserProfile.getUser().getLastName()+ " is neighbor now!")
                    .profile(toFollowUserProfile)
                    .build();

            sendNotiMessage(notificationNeighbors2);
        }
    }

    @Scheduled(cron = "0 0 0 * * ?") // Run every day at 12 AM
    public void sendBirthdayNotifications(){
        LocalDate today = LocalDate.now();
        List<User> usersWithBirthDay = userRepository.findByDateOfBirth(today);
        usersWithBirthDay.forEach(user -> {
            String messageForBDUser = "Happy Birthday "+user.getFirstName()+" "+user.getLastName()+"!";
            String messageForNeighbors = "Happy Birthday "+user.getFirstName()+" "+user.getLastName()+"!";

            Notification notificationforNeighbor = Notification.builder()
                    .message(messageForNeighbors)
                    .isRead(false)
                    .profile(user.getProfile())
                    .createDate(LocalDateTime.now())
                    .build();

            Notification notificationforBDUser = Notification.builder()
                    .message(messageForBDUser)
                    .isRead(false)
                    .createDate(LocalDateTime.now())
                    .user(user)
                    .build();

            NotificationDto notificationDtoForBDUser = NotificationDto.builder()
                                    .message(messageForBDUser)
                                            .isRead(notificationforBDUser.isRead())
                                                    .createDate(notificationforBDUser.getCreateDate())
                                                            .build();
            NotificationDto notificationDtoForNeighbors = NotificationDto.builder()
                    .message(messageForNeighbors)
                    .isRead(notificationforNeighbor.isRead())
                    .createDate(notificationforNeighbor.getCreateDate())
                    .build();



           user.getProfile().getNeighbors().forEach(neighbors->{
               notificationforNeighbor.setUser(neighbors.getUser());
               notificationRepository.save(notificationforNeighbor);
               notificationDtoForNeighbors.setId(notificationforNeighbor.getId());
                messagingTemplate.convertAndSendToUser(String.valueOf(neighbors.getId()),"/queue/private-notification",notificationDtoForNeighbors);
            });
             notificationRepository.save(notificationforBDUser);
             notificationDtoForBDUser.setId(notificationforBDUser.getId());
            messagingTemplate.convertAndSendToUser(String.valueOf(user.getId()),"/queue/private-notification", notificationDtoForBDUser);
        });
        
    }
}
