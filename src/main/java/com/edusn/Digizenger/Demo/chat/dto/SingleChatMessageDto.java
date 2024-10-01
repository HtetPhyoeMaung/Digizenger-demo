//package com.edusn.Digizenger.Demo.chat.dto;
//
//import com.edusn.Digizenger.Demo.auth.entity.User;
//import com.edusn.Digizenger.Demo.post.dto.UserDto;
//import com.fasterxml.jackson.annotation.JsonIgnore;
//import jakarta.persistence.JoinColumn;
//import jakarta.persistence.ManyToOne;
//import lombok.AllArgsConstructor;
//import lombok.Builder;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//
//import java.time.LocalDateTime;
//@Data
//@Builder
//@AllArgsConstructor
//@NoArgsConstructor
//public class SingleChatMessageDto {
//
//    private Long id;
//    private String message;
//    private LocalDateTime dateTime;
//    private String messageType;
//    private String chatId;
//    private Long recipientId;
//    @JsonIgnore
//    private UserDto senderId;
//}
