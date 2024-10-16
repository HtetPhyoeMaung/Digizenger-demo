package com.edusn.Digizenger.Demo.chat.service;

import com.edusn.Digizenger.Demo.auth.dto.response.Response;
import com.edusn.Digizenger.Demo.auth.entity.User;
import com.edusn.Digizenger.Demo.chat.dto.SingleChatMessageDto;
import com.edusn.Digizenger.Demo.chat.entity.SingleChatMessage;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface SingleChatMessageService {
    ResponseEntity<List<SingleChatMessageDto>> findChatMessages(User senderId, Long selectedUserId) ;
     ResponseEntity<Response> sendMessage(SingleChatMessage singleChatMessage, User user);

    ResponseEntity<Response> deleteMessage(SingleChatMessage singleChatMessage);

    ResponseEntity<Response> updateMessage(SingleChatMessage singleChatMessage);
}
