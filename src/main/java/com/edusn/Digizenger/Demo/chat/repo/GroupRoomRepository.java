package com.edusn.Digizenger.Demo.chat.repo;

import com.edusn.Digizenger.Demo.auth.entity.User;
import com.edusn.Digizenger.Demo.chat.entity.GroupRoom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GroupRoomRepository extends JpaRepository <GroupRoom,Long> {
    List<GroupRoom> findByUsersContains(User user);

}
