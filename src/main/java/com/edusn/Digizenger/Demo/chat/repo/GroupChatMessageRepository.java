package com.edusn.Digizenger.Demo.chat.repo;

import com.edusn.Digizenger.Demo.chat.entity.GroupChatMessage;
import com.edusn.Digizenger.Demo.chat.entity.GroupRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface GroupChatMessageRepository extends JpaRepository<GroupChatMessage,String> {
    List<GroupChatMessage> findGroupChatMessageByGroupRoom(GroupRoom groupRoom);
}
