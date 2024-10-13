package com.edusn.Digizenger.Demo.chat.controller;

import com.edusn.Digizenger.Demo.auth.dto.response.Response;
import com.edusn.Digizenger.Demo.auth.entity.User;
import com.edusn.Digizenger.Demo.auth.service.UserService;
import com.edusn.Digizenger.Demo.chat.dto.SingleChatMessageDto;
import com.edusn.Digizenger.Demo.chat.entity.GroupChatMessage;
import com.edusn.Digizenger.Demo.chat.entity.SingleChatMessage;
import com.edusn.Digizenger.Demo.chat.service.GroupChatMessageService;
import com.edusn.Digizenger.Demo.chat.service.SingleChatMessageService;
import com.edusn.Digizenger.Demo.exception.CustomNotFoundException;
import com.edusn.Digizenger.Demo.post.dto.UserDto;
import com.edusn.Digizenger.Demo.utilis.GetUserByRequest;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
public class ChatController {

    @Autowired
    private GetUserByRequest getUserByRequest;

    @Autowired
    private UserService userService;

    @Autowired
    private SingleChatMessageService singleChatMessageService;

    @Autowired
    private GroupChatMessageService groupChatMessageService;

    @GetMapping("/messages/{selectedUserId}")
    public ResponseEntity<List<SingleChatMessageDto>> findChatMessages(@PathVariable Long selectedUserId, HttpServletRequest request) {
        User sender= getUserByRequest.getUser(request);
        return singleChatMessageService.findChatMessages(sender,selectedUserId);
    }
    @GetMapping("/friend-list")
    public ResponseEntity<List<UserDto>> getFriendList(){
        return null;
    }

    @MessageMapping("/message")
    public ResponseEntity<Response> sendMessage(@Payload SingleChatMessage singleChatMessage, HttpServletRequest request) throws IOException {
        User sender= getUserByRequest.getUser(request);
        return singleChatMessageService.sendMessage(singleChatMessage,sender);

    }
    @MessageMapping("/message-delete")
    @SendTo("/topic/public")
    public ResponseEntity<Response> deleteMessage(@Payload SingleChatMessage singleChatMessage) throws IOException {
        return singleChatMessageService.deleteMessage(singleChatMessage);

    }
    @MessageMapping("/update-message")
    public ResponseEntity<Response> updateMessage(@Payload SingleChatMessage singleChatMessage) throws IOException {
        return singleChatMessageService.updateMessage(singleChatMessage);
    }


    @MessageMapping("/group-chat")
    public ResponseEntity<Response> sendGroupMessage(@Payload GroupChatMessage groupChatMessage,HttpServletRequest request){
        User sender= getUserByRequest.getUser(request);
        return groupChatMessageService.sendGroupMessage(groupChatMessage,sender);
    }

    @MessageMapping("/group-message-delete")
    @SendTo("/topic/public")
    public ResponseEntity<Response> deleteGroupMessage(@Payload GroupChatMessage groupChatMessage) throws IOException {
        return groupChatMessageService.deleteMessage(groupChatMessage);
    }
    @MessageMapping("/group-message-update")
    public ResponseEntity<Response> updateGroupMessage(@Payload GroupChatMessage groupChatMessage) throws IOException {
        return groupChatMessageService.updateMessage(groupChatMessage);
    }



}
