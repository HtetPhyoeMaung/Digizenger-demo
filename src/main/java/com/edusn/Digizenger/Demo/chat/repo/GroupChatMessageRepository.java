package com.edusn.Digizenger.Demo.chat.repo;

import com.edusn.Digizenger.Demo.chat.entity.GroupChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GroupChatMessageRepository extends JpaRepository<GroupChatMessage,String> {
}
