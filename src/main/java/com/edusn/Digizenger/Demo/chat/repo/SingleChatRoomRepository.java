package com.edusn.Digizenger.Demo.chat.repo;

import com.edusn.Digizenger.Demo.auth.entity.User;
import com.edusn.Digizenger.Demo.chat.entity.SingleChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
public interface SingleChatRoomRepository extends JpaRepository<SingleChatRoom,Long> {
    Optional<SingleChatRoom> findByUserAndRecipientId(User senderId, Long recipientId);
}
