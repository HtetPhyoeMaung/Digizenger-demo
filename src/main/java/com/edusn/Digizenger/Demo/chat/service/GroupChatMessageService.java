package com.edusn.Digizenger.Demo.chat.service;

import com.edusn.Digizenger.Demo.auth.entity.User;
import com.edusn.Digizenger.Demo.chat.entity.GroupChatMessage;

public interface GroupChatMessageService {
    void sendGroupMessage(GroupChatMessage groupChatMessage, User sender);
}
