package com.edusn.Digizenger.Demo.chat.service.impl;
import com.edusn.Digizenger.Demo.auth.dto.response.Response;
import com.edusn.Digizenger.Demo.auth.entity.User;
import com.edusn.Digizenger.Demo.auth.repo.UserRepository;
import com.edusn.Digizenger.Demo.chat.dto.SingleChatMessageDto;
import com.edusn.Digizenger.Demo.chat.entity.SingleChatMessage;
import com.edusn.Digizenger.Demo.chat.repo.SingleChatMessageRepository;
import com.edusn.Digizenger.Demo.chat.service.SingleChatMessageService;
import com.edusn.Digizenger.Demo.chat.service.SingleChatRoomService;
import com.edusn.Digizenger.Demo.exception.CustomNotFoundException;
import com.edusn.Digizenger.Demo.post.dto.UserDto;
import com.edusn.Digizenger.Demo.storage.StorageService;
import com.edusn.Digizenger.Demo.utilis.DateUtil;
import com.edusn.Digizenger.Demo.utilis.MapperUtil;
import com.edusn.Digizenger.Demo.utilis.UUIDUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class SingleChatMessageImpl implements SingleChatMessageService {
    @Autowired
    private SingleChatRoomService singleChatRoomService;

    @Autowired
    private SingleChatMessageRepository singleChatMessageRepository;

    @Autowired
    private StorageService storageService;

    @Autowired
    public SimpMessagingTemplate messagingTemplate;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private DateUtil dateUtil;


    @Override
    public ResponseEntity<Response> findChatMessages(User senderId, Long recipientId, int _page, int _limit) {
        Pageable pageable = PageRequest.of(_page, _limit, Sort.by(Sort.Direction.DESC, "createDate"));
        var chatId = singleChatRoomService.getChatRoomId(senderId, recipientId, false);
        Page<SingleChatMessage> singleChatMessages = chatId.map(id->singleChatMessageRepository.findByChatId(id,pageable)).orElse(Page.empty());
        singleChatMessages.forEach(singleChatMessage -> {singleChatMessage.setRead(true);});
        singleChatMessageRepository.saveAll(singleChatMessages);
       Response response= Response.builder()
               .singleChatMessageDtoList(singleChatMessages.stream()
                       .map(message -> SingleChatMessageDto.builder()
                               .id(message.getId())
                               .message(message.getMessage())
                               .createDate(message.getCreateDate())
                               .modifiedDate(message.getModifiedDate())
                               .recipientId(message.getRecipientId())
                               .isRead(message.isRead())
                               .userDto(UserDto.builder()
                                       .id(message.getUser().getId())
                                       .firstName(message.getUser().getFirstName())
                                       .lastName(message.getUser().getLastName())
                                       .profileImageUrl(message.getUser().getProfile().getProfileImageName()==null?null:storageService.getImageByName(message.getUser().getProfile().getProfileImageName()))
                                       .build()) // Create UserDto from senderId
                               .chatId(message.getChatId())
                               .type(SingleChatMessage.Type.valueOf(message.getType().name()))
                               .build())
                       .collect(Collectors.toList()))
               .statusCode(HttpStatus.OK.value())
               .build();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Transactional
    @Override
    public ResponseEntity<Response> sendMessage(SingleChatMessage singleChatMessage, User user) {
        if(singleChatMessage.getMessage()!=null && singleChatMessage.getType() != SingleChatMessage.Type.TEXT){
            String fileData = singleChatMessage.getMessage(); // This should contain the base64 data
            byte[] decodedBytes = Base64.getDecoder().decode(fileData); // Decode directly

            String fileName;
            String contentType;

            switch (singleChatMessage.getType()) {
                case IMAGE:
                    fileName = UUID.randomUUID() + ".jpg";
                    contentType = "image/jpeg";
                    break;
                case VIDEO:
                    fileName = UUID.randomUUID() + ".mp4";
                    contentType = "video/mp4";
                    break;
                case VOICE:
                    fileName = UUID.randomUUID() + ".wav";
                    contentType = "audio/wav";
                    break;
                default:
                    throw new IllegalArgumentException("Unsupported message type: " + singleChatMessage.getType());
            }
            try {
                String fileUrl = storageService.uploadFile(decodedBytes, fileName, contentType);
                singleChatMessage.setMessage(fileUrl); // Set the URL of the uploaded file
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        var chatId = singleChatRoomService
                .getChatRoomId(user,singleChatMessage.getRecipientId(), true)
                .orElseThrow(); // You can create your own dedicated exception
     SingleChatMessage savedMessage= SingleChatMessage.builder()
             .id(UUIDUtil.generateUUID())
             .user(user)
             .message(singleChatMessage.getMessage())
             .type(singleChatMessage.getType())
             .chatId(chatId)
             .createDate(LocalDateTime.now())
             .recipientId(singleChatMessage.getRecipientId())
             .build();
     SingleChatMessageDto singleChatMessageDto=SingleChatMessageDto.builder()
                                                     .id(savedMessage.getId())
                                                     .message(savedMessage.getMessage())
                                                     .type(savedMessage.getType())
                                                     .createDate(savedMessage.getCreateDate())
                                                     .userDto(UserDto.builder()
                                                                            .id(savedMessage.getUser().getId())
                                                                            .firstName(savedMessage.getUser().getFirstName())
                                                                            .lastName(savedMessage.getUser().getLastName())
                                                                            .profileImageUrl(savedMessage.getUser().getProfile().getProfileImageName()==null?null:storageService.getImageByName(savedMessage.getUser().getProfile().getProfileImageName()))
                                                                            .build())
                                                     .recipientId(savedMessage.getRecipientId())
                                                     .build();
        messagingTemplate.convertAndSendToUser(String.valueOf(singleChatMessage.getRecipientId()),"/queue/messages" , singleChatMessageDto);
        singleChatMessageRepository.save(savedMessage);
        Response response=Response.builder()
                .statusCode(HttpStatus.OK.value())
                .message("Message send success")
                            .build();
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Response> deleteMessage(SingleChatMessage singleChatMessage) {
            singleChatMessageRepository.delete(singleChatMessage);
        Response response=Response.builder()
                .statusCode(HttpStatus.OK.value())
                .message("Message delete success")
                .build();
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Response> updateMessage(SingleChatMessage singleChatMessage) {
        SingleChatMessage existMessage=singleChatMessageRepository.findById(singleChatMessage.getId())
                                        .orElseThrow(()->new CustomNotFoundException("Message not found with this id"));
        existMessage.setMessage(singleChatMessage.getMessage());
        existMessage.setModifiedDate(LocalDateTime.now());
        SingleChatMessage updateMessage=singleChatMessageRepository.save(existMessage);
        SingleChatMessageDto singleChatMessageDto=SingleChatMessageDto.builder()
                .id(updateMessage.getId())
                .message(updateMessage.getMessage())
                .type(updateMessage.getType())
                .createDate(updateMessage.getCreateDate())
                .modifiedDate(updateMessage.getModifiedDate())
                .recipientId(updateMessage.getRecipientId())
                .build();
        messagingTemplate.convertAndSendToUser(String.valueOf(singleChatMessage.getRecipientId()),"/queue/messages" , singleChatMessageDto);
        Response response=Response.builder()
                .statusCode(HttpStatus.OK.value())
                .message("Message update success")
                .build();
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Response> getFriendAndNonUserList(User user) {
        List<SingleChatMessage> messages = singleChatMessageRepository.findByUser(user);
    Set<Long> uniqueUserIds=new HashSet<>(); messages.forEach(message->{ uniqueUserIds.add(message.getRecipientId()); });
    List<User> uniqueRecipients = userRepository.findAllById(uniqueUserIds);
    List<UserDto> userDtos = uniqueRecipients.stream() .map(recipient -> {
         UserDto userDto = MapperUtil.convertToUserDto(recipient);
        userDto.setLastLoginTime(dateUtil.formattedDate(user.getLastLoginTime()));
        userDto.setProfileDto(MapperUtil.convertToProfileDto(recipient.getProfile()));
        messages.stream().filter(message -> message.getRecipientId().equals(recipient.getId()))
                .max(Comparator.comparing(SingleChatMessage::getCreateDate)).ifPresent(lastMessage -> userDto.setLastMessage(lastMessage.getMessage()));
        if (recipient.getProfile().getProfileImageName() != null) {
         userDto.getProfileDto().setProfileImageUrl( storageService.getImageByName(recipient.getProfile().getProfileImageName()) );
         }
         return userDto;
        }).collect(Collectors.toList());
            Response response=Response.builder()
                    .statusCode(HttpStatus.OK.value())
                    .userDtoList(userDtos)
                    .message("Chat List Success")
                    .build();

          return new ResponseEntity<>(response,HttpStatus.OK);
    }

}