package com.edusn.Digizenger.Demo.chat.repo;

import com.edusn.Digizenger.Demo.auth.entity.User;
import com.edusn.Digizenger.Demo.chat.entity.SingleChatMessage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
public interface SingleChatMessageRepository extends JpaRepository<SingleChatMessage,String> {
    Page<SingleChatMessage> findByChatId(String chatId, Pageable pageable);

    List<SingleChatMessage> findByUser(User user);
}
