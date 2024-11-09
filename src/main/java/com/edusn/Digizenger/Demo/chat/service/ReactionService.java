package com.edusn.Digizenger.Demo.chat.service;

import com.edusn.Digizenger.Demo.auth.dto.response.Response;
import com.edusn.Digizenger.Demo.chat.dto.request.ReactionRequest;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;

public interface ReactionService {
    ResponseEntity<Response> makeReact(ReactionRequest.ChatType chatType, String messageId, String emojiUtf8, Long userId);
}
