package com.edusn.Digizenger.Demo.chat.service.impl;
import com.edusn.Digizenger.Demo.auth.dto.response.Response;
import com.edusn.Digizenger.Demo.auth.entity.User;
import com.edusn.Digizenger.Demo.chat.dto.SingleChatMessageDto;
import com.edusn.Digizenger.Demo.chat.entity.SingleChatMessage;
import com.edusn.Digizenger.Demo.chat.repo.SingleChatMessageRepository;
import com.edusn.Digizenger.Demo.chat.service.SingleChatMessageService;
import com.edusn.Digizenger.Demo.chat.service.SingleChatRoomService;
import com.edusn.Digizenger.Demo.exception.CustomNotFoundException;
import com.edusn.Digizenger.Demo.post.dto.UserDto;
import com.edusn.Digizenger.Demo.storage.StorageService;
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


    @Override
    public ResponseEntity<List<SingleChatMessageDto>> findChatMessages(User senderId, Long recipientId) {
        var chatId = singleChatRoomService.getChatRoomId(senderId, recipientId, false);
        List<SingleChatMessage> singleChatMessages = chatId.map(singleChatMessageRepository::findByChatId).orElse(new ArrayList<>());
        List<SingleChatMessageDto> singleChatMessageDtos = singleChatMessages.stream()
                .map(message -> SingleChatMessageDto.builder()
                        .id(message.getId())
                        .message(message.getMessage())
                        .createDate(message.getCreateDate())
                        .modifiedDate(message.getModifiedDate())
                        .recipientId(message.getRecipientId())
                        .userDto(UserDto.builder()
                                .id(message.getUser().getId())
                                .firstName(message.getUser().getFirstName())
                                .lastName(message.getUser().getLastName())
                                .profileImageUrl(message.getUser().getProfile().getProfileImageName()==null?null:storageService.getImageByName(message.getUser().getProfile().getProfileImageName()))
                                .build()) // Create UserDto from senderId

                        .chatId(message.getChatId())
                        .type(SingleChatMessage.Type.valueOf(message.getType().name()))
                        .build())
                .collect(Collectors.toList());


        return new ResponseEntity<>(singleChatMessageDtos, HttpStatus.OK);
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
     SingleChatMessage savedMessage= singleChatMessageRepository.save(SingleChatMessage.builder()
                                            .user(user)
                                            .message(singleChatMessage.getMessage())
                                            .type(singleChatMessage.getType())
                                            .chatId(chatId)
                                            .createDate(LocalDateTime.now())
                                            .recipientId(singleChatMessage.getRecipientId())
                                            .build());
     SingleChatMessageDto singleChatMessageDto=SingleChatMessageDto.builder()
                                                        .id(savedMessage.getId())
                                                     .message(savedMessage.getMessage())
                                                     .type(savedMessage.getType())
                                                     .createDate(savedMessage.getCreateDate())
                                                     .recipientId(savedMessage.getRecipientId())
                                                     .build();
        messagingTemplate.convertAndSendToUser(String.valueOf(singleChatMessage.getRecipientId()),"/queue/messages" , singleChatMessageDto);
                                                     
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

}