package com.edusn.Digizenger.Demo.chat.service;

import com.edusn.Digizenger.Demo.auth.dto.response.Response;
import com.edusn.Digizenger.Demo.auth.entity.User;
import com.edusn.Digizenger.Demo.chat.entity.GroupRoom;
import org.springframework.http.ResponseEntity;

public interface GroupRoomService {
    ResponseEntity<Response> createGroupRoom(GroupRoom groupRoom);

    ResponseEntity<Response> removeUser(Long groupRoomId, Long userId);

    ResponseEntity<Response> inviteUser(Long groupRoomId, Long userId);

    ResponseEntity<Response> deleteGroup(Long groupRoomId);

    ResponseEntity<Response> groupList(User user);
}
