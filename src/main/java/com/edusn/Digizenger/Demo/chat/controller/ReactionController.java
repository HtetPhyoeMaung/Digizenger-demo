package com.edusn.Digizenger.Demo.chat.controller;

import com.edusn.Digizenger.Demo.chat.service.ReactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ReactionController {
    private final ReactionService reactionService;

}
