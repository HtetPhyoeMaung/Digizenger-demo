package com.edusn.Digizenger.Demo.chat.controller;

import com.edusn.Digizenger.Demo.auth.entity.User;
import com.edusn.Digizenger.Demo.auth.service.UserService;
import com.edusn.Digizenger.Demo.chat.dto.SingleChatMessageDto;
import com.edusn.Digizenger.Demo.chat.entity.SingleChatMessage;
import com.edusn.Digizenger.Demo.chat.service.SingleChatMessageService;
import com.edusn.Digizenger.Demo.exception.CustomNotFoundException;
import com.edusn.Digizenger.Demo.utilis.GetUserByRequest;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/chat")
public class ChatController {

    @Autowired
    private GetUserByRequest getUserByRequest;
    @Autowired
    private UserService userService;

    @Autowired
    private SingleChatMessageService singleChatMessageService;

    @GetMapping("/messages/{selectedUserId}")
    public ResponseEntity<List<SingleChatMessageDto>> findChatMessages(@PathVariable Long selectedUserId, HttpServletRequest request) {
        User senderId= getUserByRequest.getUser(request);


        return singleChatMessageService.findChatMessages(senderId,selectedUserId);
    }

    @MessageMapping("/message")
    public void processMessage(@Payload SingleChatMessage singleChatMessage) throws IOException {
        User user= userService.findById(singleChatMessage.getSendId()).orElseThrow(()->new CustomNotFoundException("User not found by"+singleChatMessage.getSendId()));
        singleChatMessageService.sendMessage(singleChatMessage,user);
    }
}
