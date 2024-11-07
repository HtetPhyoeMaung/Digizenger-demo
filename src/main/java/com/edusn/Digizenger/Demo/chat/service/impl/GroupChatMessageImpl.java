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
import com.edusn.Digizenger.Demo.utilis.DateUtil;
import com.edusn.Digizenger.Demo.utilis.MapperUtil;
import com.edusn.Digizenger.Demo.utilis.UUIDUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;
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
    @Autowired
    private DateUtil dateUtil;
    @Autowired
    private MapperUtil mapperUtil;
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
        GroupChatMessage savedMessage=GroupChatMessage.builder()
                                                                .id(UUIDUtil.generateUUID())
                                                                .user(sender)
                                                                .groupRoom(groupRoom)
                                                                .message(groupChatMessage.getMessage())
                                                                .createDate(LocalDateTime.now())
                                                                .type(groupChatMessage.getType())
                                                                .build();
        GroupChatMessageDto groupChatMessageDto=GroupChatMessageDto.builder()
                                                .id(savedMessage.getId())
                                                .message(savedMessage.getMessage())
                                                .type(savedMessage.getType())
                                                .createDate(dateUtil.formattedDate(savedMessage.getCreateDate()))
                                                .modifiedDate(dateUtil.formattedDate(savedMessage.getModifiedDate()))
                                                .build();
        messagingTemplate.convertAndSendToUser(String.valueOf(groupChatMessage.getGroupRoom().getId()),"/queue/group-messages" , groupChatMessageDto);
        groupChatMessageRepository.save(savedMessage);
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
                .statusCode(HttpStatus.NO_CONTENT.value())
                .message("Group Message Delete success")
                .build();
        return new ResponseEntity<>(response,HttpStatus.NO_CONTENT);
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
                                                .message(savedMessage.getMessage())
                                                .type(savedMessage.getType())
                                                .createDate(dateUtil.formattedDate(savedMessage.getCreateDate()))
                                                .modifiedDate(savedMessage.getModifiedDate()!=null?dateUtil.formattedDate(savedMessage.getModifiedDate()):"")
                                                .build();
        messagingTemplate.convertAndSendToUser(String.valueOf(groupChatMessage.getGroupRoom().getId()),"/queue/group-messages" , groupChatMessageDto);
        Response response=Response.builder()
                                    .statusCode(HttpStatus.OK.value())
                                    .message("Group Message update success")
                                    .build();
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Response> getGroupChatMessageList(Long roomId, int page, int limit) {
        GroupRoom groupRoom=groupRoomRepository.findById(roomId).orElseThrow(()->new CustomNotFoundException("Group is not found by "+roomId+"!"));
        List<GroupChatMessage>groupChatMessageList=groupChatMessageRepository.findGroupChatMessageByGroupRoom(groupRoom);
        List<GroupChatMessageDto> groupChatMessageDtoList=groupChatMessageList.stream().map(groupChatMessage -> GroupChatMessageDto.builder()
                                                            .id(groupChatMessage.getId())
                                                            .type(groupChatMessage.getType())
                                                            .message(groupChatMessage.getMessage())
                                                            .createDate(dateUtil.formattedDate(groupChatMessage.getCreateDate()))
                                                            .modifiedDate(groupChatMessage.getModifiedDate()!=null?dateUtil.formattedDate(groupChatMessage.getModifiedDate()):"")
                                                            .userDto(mapperUtil.convertToUserDto(groupChatMessage.getUser(),true))
                                                            .build())
                                                            .toList();
        Response response=Response.builder()
                            .statusCode(HttpStatus.OK.value())
                            .groupChatMessageDtoList(groupChatMessageDtoList)
                            .message("Group Message List success")
                            .build();
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

}
