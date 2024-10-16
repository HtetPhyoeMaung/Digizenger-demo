package com.edusn.Digizenger.Demo.chat.service.impl;

import com.edusn.Digizenger.Demo.auth.dto.response.Response;
import com.edusn.Digizenger.Demo.auth.entity.User;
import com.edusn.Digizenger.Demo.chat.dto.GroupChatMessageDto;
import com.edusn.Digizenger.Demo.chat.entity.GroupChatMessage;
import com.edusn.Digizenger.Demo.chat.entity.GroupRoom;
import com.edusn.Digizenger.Demo.chat.repo.GroupChatMessageRepository;
import com.edusn.Digizenger.Demo.chat.repo.GroupRoomRepository;
import com.edusn.Digizenger.Demo.chat.service.GroupChatMessageService;
import com.edusn.Digizenger.Demo.exception.CustomNotFoundException;
import com.edusn.Digizenger.Demo.storage.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.UUID;

@Service
public class GroupChatMessageImpl implements GroupChatMessageService {
    @Autowired
    private StorageService storageService;

    @Autowired
    public SimpMessagingTemplate messagingTemplate;

    @Autowired
    public GroupChatMessageRepository groupChatMessageRepository;
    @Autowired
    private GroupRoomRepository groupRoomRepository;

    @Override
    public ResponseEntity<Response> sendGroupMessage(GroupChatMessage groupChatMessage, User sender) {
        GroupRoom groupRoom = groupRoomRepository.findById(groupChatMessage.getGroupRoom().getId()).orElseThrow(()->new CustomNotFoundException("Group is not found by this id"));
        if (groupChatMessage.getMessage() != null && groupChatMessage.getType() != GroupChatMessage.Type.TEXT) {
            String fileData = groupChatMessage.getMessage();  // Contains base64 data directly
            byte[] decodedBytes = Base64.getDecoder().decode(fileData); // Decode directly

            String fileName;
            String contentType;

            switch (groupChatMessage.getType()) {
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
                    throw new IllegalArgumentException("Unsupported message type: " + groupChatMessage.getType());
            }

            try {
                String fileUrl = storageService.uploadFile(decodedBytes, fileName, contentType);
                groupChatMessage.setMessage(fileUrl); // Set the URL of the uploaded file
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        GroupChatMessage savedMessage=groupChatMessageRepository.save(GroupChatMessage.builder()
                        .user(sender)
                        .groupRoom(groupRoom)
                        .message(groupChatMessage.getMessage())
                        .createDate(LocalDateTime.now())
                        .type(groupChatMessage.getType())
                         .build());
        GroupChatMessageDto groupChatMessageDto=GroupChatMessageDto.builder()
                                                .id(savedMessage.getId())
                                                .text(savedMessage.getMessage())
                                                .type(savedMessage.getType())
                                                .createDate(savedMessage.getCreateDate())
                                                .modifiedDate(savedMessage.getModifiedDate())
                                                .build();
        messagingTemplate.convertAndSendToUser(String.valueOf(groupChatMessage.getGroupRoom().getId()),"/queue/group-messages" , groupChatMessageDto);
        Response response=Response.builder()
                .statusCode(HttpStatus.OK.value())
                .message("Group Message send success")
                .build();
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Response> deleteMessage(GroupChatMessage groupChatMessage) {
        groupChatMessageRepository.delete(groupChatMessage);
        Response response=Response.builder()
                .statusCode(HttpStatus.OK.value())
                .message("Group Message Delete success")
                .build();
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Response> updateMessage(GroupChatMessage groupChatMessage) {
        GroupChatMessage existGroupMessage=groupChatMessageRepository.findById(groupChatMessage.getId())
                                                .orElseThrow(()->new CustomNotFoundException("Group Message not found with this id "+groupChatMessage.getId()));
        existGroupMessage.setMessage(groupChatMessage.getMessage());
        existGroupMessage.setModifiedDate(LocalDateTime.now());
        GroupChatMessage savedMessage=groupChatMessageRepository.save(existGroupMessage);
        GroupChatMessageDto groupChatMessageDto=GroupChatMessageDto.builder()
                                                .id(savedMessage.getId())
                                                .text(savedMessage.getMessage())
                                                .type(savedMessage.getType())
                                                .createDate(savedMessage.getCreateDate())
                                                .modifiedDate(savedMessage.getModifiedDate())
                                                .build();
        messagingTemplate.convertAndSendToUser(String.valueOf(groupChatMessage.getGroupRoom().getId()),"/queue/group-messages" , groupChatMessageDto);
        Response response=Response.builder()
                                    .statusCode(HttpStatus.OK.value())
                                    .message("Group Message update success")
                                    .build();
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

}
