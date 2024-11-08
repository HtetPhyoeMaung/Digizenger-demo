package com.edusn.Digizenger.Demo.chat.controller;

import com.edusn.Digizenger.Demo.auth.dto.response.Response;
import com.edusn.Digizenger.Demo.chat.service.ReactionService;
import com.edusn.Digizenger.Demo.chat.dto.request.ReactionRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/reaction")
public class ReactionController {
    private final ReactionService reactionService;

    @MessageMapping("/messages/react")
    public ResponseEntity<Response> response(@Payload ReactionRequest reactionRequest){

        return reactionService.makeReact(reactionRequest.getChatType(),reactionRequest.getMessageId(),reactionRequest.getEmojiUtf8(),reactionRequest.getUserId());
    }

}
