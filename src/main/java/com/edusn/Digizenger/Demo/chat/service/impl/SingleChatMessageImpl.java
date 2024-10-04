package com.edusn.Digizenger.Demo.chat.service.impl;
import com.edusn.Digizenger.Demo.auth.entity.User;
import com.edusn.Digizenger.Demo.chat.dto.SingleChatMessageDto;
import com.edusn.Digizenger.Demo.chat.entity.SingleChatMessage;
import com.edusn.Digizenger.Demo.chat.repo.SingleChatMessageRepository;
import com.edusn.Digizenger.Demo.chat.service.SingleChatMessageService;
import com.edusn.Digizenger.Demo.chat.service.SingleChatRoomService;
import com.edusn.Digizenger.Demo.post.dto.UserDto;
import com.edusn.Digizenger.Demo.storage.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.UUID;
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
        var chatId = singleChatRoomService.getChatRoomId(senderId, recipientId, true);
        List<SingleChatMessage> singleChatMessages = chatId.map(singleChatMessageRepository::findByChatId).orElse(new ArrayList<>());
        List<SingleChatMessageDto> singleChatMessageDtos = singleChatMessages.stream()
                .map(message -> SingleChatMessageDto.builder()
                        .message(message.getMessage())
                        .createDate(message.getCreateDate())
                        .modifiedDate(message.getModifiedDate())
                        .recipientId(message.getRecipientId())
                        .userDto(UserDto.builder()
                                .id(message.getUser().getId())
                                .firstName(message.getUser().getFirstName())
                                .lastName(message.getUser().getLastName())
                                .build()) // Create UserDto from senderId
                        .chatId(message.getChatId())
                        .type(SingleChatMessageDto.Type.valueOf(message.getType().name()))
                        .build())
                .collect(Collectors.toList());
        return new ResponseEntity<>(singleChatMessageDtos, HttpStatus.OK);
    }

    @Override
    public void sendMessage(SingleChatMessage singleChatMessage,User user) {
        singleChatMessage.setUser(user);
        if (singleChatMessage.getType() == SingleChatMessage.Type.IMAGE || singleChatMessage.getType() == SingleChatMessage.Type.VIDEO || singleChatMessage.getType() == SingleChatMessage.Type.VOICE) {
            String fileData = singleChatMessage.getPhotoOrVideo(); // This should contain the base64 data
            String fileName = UUID.randomUUID() + (singleChatMessage.getType() == SingleChatMessage.Type.VOICE ? ".wav" : ".file"); // Generate a unique filename based on your logic
            byte[] decodedBytes = Base64.getDecoder().decode(fileData.split(",")[1]); // Get the data part
            String contentType;
            switch (singleChatMessage.getType()) {
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
                    throw new IllegalArgumentException("Unsupported message type: " + singleChatMessage.getType());
            }
            try {
                String fileUrl = storageService.uploadFile(decodedBytes, fileName, contentType);
                singleChatMessage.setPhotoOrVideo(fileUrl); // Set the URL of the uploaded file
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        var chatId = singleChatRoomService
                .getChatRoomId(singleChatMessage.getUser(),singleChatMessage.getRecipientId(), true)
                .orElseThrow(); // You can create your own dedicated exception
        singleChatMessage.setChatId(chatId);
        singleChatMessage.setCreateDate(LocalDateTime.now());
        singleChatMessageRepository.save(singleChatMessage);
        messagingTemplate.convertAndSend("/topic/chat/" + singleChatMessage.getRecipientId(), singleChatMessage);
    }

}