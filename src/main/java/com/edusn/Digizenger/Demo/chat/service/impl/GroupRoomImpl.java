package com.edusn.Digizenger.Demo.chat.service.impl;

import com.edusn.Digizenger.Demo.auth.dto.response.Response;
import com.edusn.Digizenger.Demo.auth.entity.User;
import com.edusn.Digizenger.Demo.auth.repo.UserRepository;
import com.edusn.Digizenger.Demo.chat.dto.GroupRoomDto;
import com.edusn.Digizenger.Demo.chat.entity.GroupRoom;
import com.edusn.Digizenger.Demo.chat.repo.GroupRoomRepository;
import com.edusn.Digizenger.Demo.chat.service.GroupRoomService;
import com.edusn.Digizenger.Demo.exception.CustomNotFoundException;
import com.edusn.Digizenger.Demo.post.dto.UserDto;
import com.edusn.Digizenger.Demo.utilis.MapperUtil;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GroupRoomImpl implements GroupRoomService {
    @Autowired
    private GroupRoomRepository groupRoomRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private MapperUtil mapperUtil;
    @Override
    @Transactional
    public ResponseEntity<Response> createGroupRoom(GroupRoom groupRoom) {
        groupRoom.setCreateDate(LocalDateTime.now());
        List<Long> userIds = groupRoom.getUsers().stream()
                .map(User::getId)
                .toList();
        List<User> users = userRepository.findAllById(userIds);
        List<UserDto> userDtoList = new LinkedList<>();
        GroupRoom createdGroup=groupRoomRepository.save(groupRoom);
        for (User user : users) {
            user.getGroupRooms().add(createdGroup);
            userDtoList.add(modelMapper.map(user, UserDto.class));
        }
        GroupRoomDto groupRoomDto = modelMapper.map(groupRoom, GroupRoomDto.class);
        groupRoomDto.setUserDtoList(userDtoList);
        Response response=Response.builder()
                .statusCode(HttpStatus.CREATED.value())
                .message("Group Room Create Success")
                .groupRoomDto(groupRoomDto)
                .build();
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Override
    @Transactional
    public ResponseEntity<Response> removeUser(Long groupRoomId, Long userId) {
        GroupRoom groupRoom=groupRoomRepository.findById(groupRoomId).orElseThrow(()->new CustomNotFoundException("Group is not found by this id"));
        User user=userRepository.findById(userId).orElseThrow(()->new CustomNotFoundException("User is not found by this id"));
        groupRoom.getUsers().remove(user);
        user.getGroupRooms().remove(groupRoom);
        List<UserDto> userDtoList = groupRoom.getUsers().stream()
                .map(userInGroup -> modelMapper.map(userInGroup, UserDto.class))
                .toList();
        GroupRoomDto groupRoomDto = modelMapper.map(groupRoom, GroupRoomDto.class);
        groupRoomDto.setUserDtoList(userDtoList);
        Response response=Response.builder()
                .statusCode(HttpStatus.OK.value())
                .message("User Remove Success")
                .groupRoomDto(groupRoomDto)
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Response> inviteUser(Long groupRoomId, Long userId) {
        GroupRoom groupRoom=groupRoomRepository.findById(groupRoomId).orElseThrow(()->new CustomNotFoundException("Group is not found by this id"));
        User user=userRepository.findById(userId).orElseThrow(()->new CustomNotFoundException("User is not found by this id"));
        if (groupRoom.getUsers().contains(user)) {
            Response response=Response.builder()
                    .statusCode(HttpStatus.BAD_REQUEST.value())
                    .message("This User Already Exist in group")
                    .build();
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
            groupRoom.getUsers().add(user);
        user.getGroupRooms().add(groupRoom);
        List<UserDto> userDtoList = groupRoom.getUsers().stream()
                .map(user1 -> modelMapper.map(user1, UserDto.class))
                .toList();
        groupRoomRepository.save(groupRoom);
        GroupRoomDto groupRoomDto = modelMapper.map(groupRoom, GroupRoomDto.class);
        groupRoomDto.setUserDtoList(userDtoList);
        Response response=Response.builder()
                .statusCode(HttpStatus.OK.value())
                .message("User invite Success")
                .groupRoomDto(groupRoomDto)
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Response> deleteGroup(Long groupRoomId) {
        GroupRoom groupRoom = groupRoomRepository.findById(groupRoomId)
                .orElseThrow(() -> new CustomNotFoundException("Group is not found by this id"));
        for (User user : groupRoom.getUsers()) {
            user.getGroupRooms().remove(groupRoom);
        }
        groupRoomRepository.delete(groupRoom);
        Response response = Response.builder()
                .statusCode(HttpStatus.OK.value())
                .message("Group Room deleted successfully")
                .build();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Response> groupList(User user) {
    List<GroupRoom> groupRooms=groupRoomRepository.findByUsersContains(user);
        List<GroupRoomDto> groupRoomDtoList = groupRooms.stream()
                .map(groupRoom -> GroupRoomDto.builder()
                        .id(groupRoom.getId())
                        .groupName(groupRoom.getGroupName())
                        .createDate(groupRoom.getCreateDate())
                        .userDtoList(groupRoom.getUsers().stream()
                                .map(groupUser -> UserDto.builder()
                                        .id(groupUser.getId())
                                        .firstName(groupUser.getFirstName())
                                        .lastName(groupUser.getLastName())
                                        .userProfileUrlLink(groupUser.getProfile().getProfileLinkUrl())  // Assuming this field exists
                                        .followers(groupUser.getFollowers())    // Assuming followers is a list or set
                                        .build())
                                .collect(Collectors.toList()))
                        .build())
                        .toList();
        Response response = Response.builder()
                .statusCode(HttpStatus.OK.value())
                .groupRoomDtoList(groupRoomDtoList)
                .message("Group Room List")
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
