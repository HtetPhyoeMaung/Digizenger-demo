//package com.edusn.Digizenger.Demo.chat.entity;
//
//import com.edusn.Digizenger.Demo.auth.entity.User;
//import com.fasterxml.jackson.annotation.JsonIgnore;
//import jakarta.persistence.*;
//import lombok.AllArgsConstructor;
//import lombok.Builder;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//
//import java.time.LocalDateTime;
//
//@Entity
//@Data
//@Builder
//@NoArgsConstructor
//@AllArgsConstructor
//@Table(name = "one_to_one_message")
//public class SingleChatMessage {
//        private Long id;
//        private String message;
//        private LocalDateTime dateTime;
//        private String messageType;
//        private String chatId;
//        private Long recipientId;
//        @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
//        @JoinColumn(name = "sender_id")
//        private User senderId;
//
//}
