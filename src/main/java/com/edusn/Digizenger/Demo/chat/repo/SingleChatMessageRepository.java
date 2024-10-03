package com.edusn.Digizenger.Demo.chat.repo;

import com.edusn.Digizenger.Demo.chat.entity.SingleChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
public interface SingleChatMessageRepository extends JpaRepository<SingleChatMessage,Long> {
    List<SingleChatMessage>  findByChatId(String chatId);
}
