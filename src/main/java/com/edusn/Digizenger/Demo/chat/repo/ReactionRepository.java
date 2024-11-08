package com.edusn.Digizenger.Demo.chat.repo;

import com.edusn.Digizenger.Demo.chat.entity.Reaction;
import org.springframework.data.jpa.repository.JpaRepository;



public interface ReactionRepository extends JpaRepository<Reaction, Long> {
    Reaction findBySingleChatMessageIdAndUserId(String singleChatMessageId, Long reactedUser);
    Reaction findByGroupChatMessageIdAndUserId(String GroupChatMessageId, Long reactedUserId);
}