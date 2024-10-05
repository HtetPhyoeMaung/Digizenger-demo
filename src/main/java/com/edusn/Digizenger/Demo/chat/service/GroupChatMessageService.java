package com.edusn.Digizenger.Demo.chat.service;

import com.edusn.Digizenger.Demo.auth.dto.response.Response;
import com.edusn.Digizenger.Demo.auth.entity.User;
import com.edusn.Digizenger.Demo.chat.entity.GroupChatMessage;
import org.springframework.http.ResponseEntity;

public interface GroupChatMessageService {
    ResponseEntity<Response> sendGroupMessage(GroupChatMessage groupChatMessage, User sender);
}
