package com.edusn.Digizenger.Demo.chat.controller;

import com.edusn.Digizenger.Demo.auth.dto.response.Response;
import com.edusn.Digizenger.Demo.auth.entity.User;
import com.edusn.Digizenger.Demo.auth.repo.UserRepository;
import com.edusn.Digizenger.Demo.auth.service.AuthService;
import com.edusn.Digizenger.Demo.chat.dto.SingleChatMessageDto;
import com.edusn.Digizenger.Demo.chat.entity.GroupChatMessage;
import com.edusn.Digizenger.Demo.chat.entity.SingleChatMessage;
import com.edusn.Digizenger.Demo.chat.service.GroupChatMessageService;
import com.edusn.Digizenger.Demo.chat.service.SingleChatMessageService;
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
@RequestMapping("/chat")
public class ChatController {

    @Autowired
    private GetUserByRequest getUserByRequest;

    @Autowired
    private SingleChatMessageService singleChatMessageService;

    @Autowired
    private GroupChatMessageService groupChatMessageService;
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/messages/{selectedUserId}")
    public ResponseEntity<Response> findChatMessages(@PathVariable Long selectedUserId, HttpServletRequest request,
                                                                       @RequestParam(value = "_page",defaultValue = "1") int _page,
                                                                       @RequestParam(value = "_limit",defaultValue = "10") int _limit) {
        User sender= getUserByRequest.getUser(request);
        return singleChatMessageService.findChatMessages(sender,selectedUserId, _page-1, _limit);
    }

    @MessageMapping("/message")
    public ResponseEntity<Response> sendMessage(@Payload SingleChatMessage singleChatMessage) throws IOException {
        User sender= userRepository.findById(singleChatMessage.getUser().getId()).orElseThrow();
        return singleChatMessageService.sendMessage(singleChatMessage,sender);
    }

    @GetMapping("/chat-list")
    public ResponseEntity<Response> getFriendAndNonUserList(HttpServletRequest request) {
        User user= getUserByRequest.getUser(request);
        return singleChatMessageService.getFriendAndNonUserList(user);
    }

    @MessageMapping("/message-delete")
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
    public ResponseEntity<Response> deleteGroupMessage(@Payload GroupChatMessage groupChatMessage) throws IOException {
        return groupChatMessageService.deleteMessage(groupChatMessage);
    }

    @MessageMapping("/group-message-update")
    public ResponseEntity<Response> updateGroupMessage(@Payload GroupChatMessage groupChatMessage) throws IOException {
        return groupChatMessageService.updateMessage(groupChatMessage);
    }
  
    @GetMapping("/group-message-list/{roomId}")
    public  ResponseEntity<Response>groupMessageList(@PathVariable("roomId") Long roomId, @RequestParam(value = "_page",defaultValue = "1") int _page,
                                                     @RequestParam(value = "_limit",defaultValue = "10") int _limit){
        return groupChatMessageService.getGroupChatMessageList(roomId,_page,_limit);
    }


}
