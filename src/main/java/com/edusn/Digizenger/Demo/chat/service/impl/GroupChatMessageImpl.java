package com.edusn.Digizenger.Demo.chat.service.impl;

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
    public void sendGroupMessage(GroupChatMessage groupChatMessage, User sender) {
        GroupRoom groupRoom = groupRoomRepository.findById(groupChatMessage.getGroupRoom().getId()).orElseThrow(()->new CustomNotFoundException("Group is not found by this id"));
        if(groupChatMessage.getMessage()!=null && groupChatMessage.getType() != GroupChatMessage.Type.TEXT){
            String fileData = groupChatMessage.getMessage(); // This should contain the base64 data
            String fileName = UUID.randomUUID() + (groupChatMessage.getType() == GroupChatMessage.Type.VOICE ? ".wav" : ".file"); // Generate a unique filename based on your logic
            byte[] decodedBytes = Base64.getDecoder().decode(fileData.split(",")[1]); // Get the data part
            String contentType;
            switch (groupChatMessage.getType()) {
                case IMAGE:
                    contentType = "image/jpeg"; // Adjust based on your image format
                    fileName = UUID.randomUUID() + ".jpg"; // Use appropriate extension for images
                    break;
                case VIDEO:
                    contentType = "video/mp4"; // Adjust based on your video format
                    fileName = UUID.randomUUID() + ".mp4"; // Use appropriate extension for videos
                    break;
                case VOICE:
                    contentType = "audio/wav"; // Change if you support different audio formats
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
    }

}
