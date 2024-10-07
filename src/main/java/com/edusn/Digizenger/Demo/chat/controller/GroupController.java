package com.edusn.Digizenger.Demo.chat.controller;

import com.edusn.Digizenger.Demo.auth.dto.response.Response;
import com.edusn.Digizenger.Demo.auth.entity.User;
import com.edusn.Digizenger.Demo.chat.dto.SingleChatMessageDto;
import com.edusn.Digizenger.Demo.chat.entity.GroupRoom;
import com.edusn.Digizenger.Demo.chat.service.GroupRoomService;
import com.edusn.Digizenger.Demo.utilis.GetUserByRequest;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/group")
public class GroupController {
    @Autowired
    private GetUserByRequest getUserByRequest;

    @Autowired
    private GroupRoomService groupRoomService;

    @PostMapping("/create-group-room")
    public ResponseEntity<Response> createGroupRoom(@RequestBody GroupRoom groupRoom) {
        return groupRoomService.createGroupRoom(groupRoom);
    }

    @DeleteMapping("/{groupRoomId}/remove-user/{userId}")
    public ResponseEntity<Response> removerUserFromGroup(@PathVariable("groupRoomId") Long groupRoomId,@PathVariable("userId") Long userId){
        return groupRoomService.removeUser(groupRoomId,userId);
    }

    @PostMapping("/{groupRoomId}/invite-user/{userId}")
    public ResponseEntity<Response> inviteUserForGroup(@PathVariable("groupRoomId") Long groupRoomId,@PathVariable("userId") Long userId){
        return groupRoomService.inviteUser(groupRoomId,userId);
    }

    @DeleteMapping("/delete-group-room/{groupRoomId}")
    public ResponseEntity<Response> deleteGroup(@PathVariable("groupRoomId") Long groupRoomId){
        return groupRoomService.deleteGroup(groupRoomId);
    }

    @GetMapping("/group-list")
    public ResponseEntity<Response> groupList(HttpServletRequest request){
        User user= getUserByRequest.getUser(request);
        return groupRoomService.groupList(user);
    }

    @PutMapping("/update-group")
    public ResponseEntity<Response> updateGroup(@RequestBody GroupRoom groupRoom){
        return groupRoomService.updateGroup(groupRoom);
    }

    }
