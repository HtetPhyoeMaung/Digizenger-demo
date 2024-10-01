//package com.edusn.Digizenger.Demo.chat.entity;
//
//import com.edusn.Digizenger.Demo.auth.entity.User;
//import jakarta.persistence.*;
//import lombok.AllArgsConstructor;
//import lombok.Builder;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//
//@Entity
//@Data
//@Builder
//@NoArgsConstructor
//@AllArgsConstructor
//@Table(name = "one_to_one_chatroom")
//public class SingleChatRoom {
//    private Long id;
//    private String chatRoomId;
//    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
//    @JoinColumn(name = "sender_id")
//    private User senderId;
//
//    private Long recipientId;
//}
