package com.edusn.Digizenger.Demo.chat.service;

import com.edusn.Digizenger.Demo.auth.entity.User;
import com.edusn.Digizenger.Demo.chat.dto.SingleChatMessageDto;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
public interface SingleChatRoomService {


    public Optional<String> getChatRoomId(User senderId, Long recipientId, boolean createNewRoomIfNotExists) ;


}
