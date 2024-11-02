package com.edusn.Digizenger.Demo.chat.service.impl;

import com.edusn.Digizenger.Demo.auth.entity.User;
import com.edusn.Digizenger.Demo.auth.repo.UserRepository;
import com.edusn.Digizenger.Demo.auth.service.AuthService;
import com.edusn.Digizenger.Demo.chat.entity.SingleChatRoom;
import com.edusn.Digizenger.Demo.chat.repo.SingleChatRoomRepository;
import com.edusn.Digizenger.Demo.chat.service.SingleChatRoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class SingleChatRoomImpl implements SingleChatRoomService {
    @Autowired
    private SingleChatRoomRepository singleChatRoomRepository;
    @Autowired
    private UserRepository userRepository;

    @Override
    public Optional<String> getChatRoomId(User senderId, Long recipientId, boolean createNewRoomIfNotExists) {
    if(senderId ==null || recipientId ==0){
                 throw new IllegalArgumentException("Sender Id and Recipiet Id must not be empty");
    }
    Optional<SingleChatRoom>existingRoom=singleChatRoomRepository.findByUserAndRecipientId(senderId,recipientId);
    if(existingRoom.isPresent()){
         return Optional.of(existingRoom.get().getChatRoomId());
    }
    if(createNewRoomIfNotExists){
        String chatRoomId=creatChatId(senderId,recipientId);
        return Optional.of(chatRoomId);
    }
        return Optional.empty();
    }

    private String creatChatId(User senderId, Long recipientId) {
        String chatId = String.format("%s_%s", senderId.getId(), recipientId);
        SingleChatRoom senderRecipient =SingleChatRoom.builder()
                                            .chatRoomId(chatId)
                                            .user(senderId)
                                            .recipientId(recipientId)
                                            .build();
        Optional<User> receipient= userRepository.findById(recipientId);
        SingleChatRoom recipientSender =SingleChatRoom.builder()
                                            .chatRoomId(chatId)
                                            .user(receipient.get())
                                            .recipientId(senderId.getId())
                                            .build();
        singleChatRoomRepository.save(senderRecipient);
        singleChatRoomRepository.save(recipientSender);
        return chatId;
    }


}
