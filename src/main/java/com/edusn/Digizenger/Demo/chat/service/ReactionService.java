package com.edusn.Digizenger.Demo.chat.service;

import com.edusn.Digizenger.Demo.auth.dto.response.Response;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;

public interface ReactionService {
    ResponseEntity<Response> makeReact(String singleMessageId,String groupMessageId, String emojiUtf8, HttpServletRequest request);
}
