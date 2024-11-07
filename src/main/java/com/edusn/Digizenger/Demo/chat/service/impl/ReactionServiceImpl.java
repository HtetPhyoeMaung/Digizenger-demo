package com.edusn.Digizenger.Demo.chat.service.impl;

import com.edusn.Digizenger.Demo.auth.dto.response.Response;
import com.edusn.Digizenger.Demo.auth.entity.User;
import com.edusn.Digizenger.Demo.auth.repo.UserRepository;
import com.edusn.Digizenger.Demo.chat.dto.GroupChatMessageDto;
import com.edusn.Digizenger.Demo.chat.dto.ReactionDto;
import com.edusn.Digizenger.Demo.chat.dto.SingleChatMessageDto;
import com.edusn.Digizenger.Demo.chat.entity.GroupChatMessage;
import com.edusn.Digizenger.Demo.chat.entity.Reaction;
import com.edusn.Digizenger.Demo.chat.entity.SingleChatMessage;
import com.edusn.Digizenger.Demo.chat.repo.GroupChatMessageRepository;
import com.edusn.Digizenger.Demo.chat.repo.ReactionRepository;
import com.edusn.Digizenger.Demo.chat.repo.SingleChatMessageRepository;
import com.edusn.Digizenger.Demo.chat.service.ReactionService;
import com.edusn.Digizenger.Demo.exception.CustomNotFoundException;
import com.edusn.Digizenger.Demo.post.dto.UserDto;
import com.edusn.Digizenger.Demo.storage.StorageService;
import com.edusn.Digizenger.Demo.utilis.DateUtil;
import com.edusn.Digizenger.Demo.utilis.GetUserByRequest;
import com.edusn.Digizenger.Demo.utilis.UUIDUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReactionServiceImpl implements ReactionService {

    private final ReactionRepository reactionRepository;
    private final SingleChatMessageRepository singleChatMessageRepository;
    private final GroupChatMessageRepository groupChatMessageRepository;
    private final DateUtil dateUtil;
    private final StorageService storageService;
    private final SimpMessagingTemplate messagingTemplate;
    private final UserRepository userRepository;
    private final GetUserByRequest getUserByRequest;

    @Override
    @Transactional
    public synchronized ResponseEntity<Response> makeReact(String singleMessageId,
                                                           String groupMessageId,
                                                           String emojiUtf8,
                                                           Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomNotFoundException("user not found"));
        String message = "";
        Reaction existSingleReaction = null;
        Reaction existGroupReaction = null;
        if(singleMessageId != null) {
            existSingleReaction = reactionRepository.findBySingleChatMessageIdAndUserId(singleMessageId,user.getId());
            if(existSingleReaction != null && existSingleReaction.getEmoji().equals(emojiUtf8)){
                existSingleReaction.getSingleChatMessage().getReactions().remove(existSingleReaction);
                singleChatMessageRepository.save(existSingleReaction.getSingleChatMessage());
                message = "successfully removed reaction.";
            }
            if(existSingleReaction != null){
                existSingleReaction.setEmoji(emojiUtf8);
                existSingleReaction.setEditedDate(LocalDateTime.now());
                reactionRepository.save(existSingleReaction);
                message = "successfully updated reaction.";

            }
        }else if(groupMessageId != null){
            existGroupReaction = reactionRepository.findByGroupChatMessageIdAndUserId(groupMessageId, user.getId());
            if(existGroupReaction != null && existGroupReaction.getEmoji().equals(emojiUtf8)){
                existGroupReaction.getSingleChatMessage().getReactions().remove(existGroupReaction);
                singleChatMessageRepository.save(existGroupReaction.getSingleChatMessage());
                message = "successfully removed reaction.";
            }
            if(existGroupReaction != null){
                existGroupReaction.setEmoji(emojiUtf8);
                existGroupReaction.setEditedDate(LocalDateTime.now());
                reactionRepository.save(existGroupReaction);
                message = "successfully updated reaction.";

            }
        }

        SingleChatMessage singleChatMessage = null;
        if(singleMessageId != null) singleChatMessage =  singleChatMessageRepository.findById(singleMessageId)
                .orElseThrow(() -> new CustomNotFoundException("One to one message cannot found in server"));

        GroupChatMessage groupChatMessage = null;
        if(groupMessageId != null) groupChatMessage = groupChatMessageRepository.findById(groupMessageId)
                .orElseThrow(() -> new CustomNotFoundException("Group message cannot found in server"));


        Reaction reaction ;
        List<ReactionDto> reactionDtoList = new ArrayList<>();

        if(existSingleReaction == null && existGroupReaction == null) {
            reaction = Reaction.builder()
                    .id(UUIDUtil.generateUUID())
                    .singleChatMessage(singleChatMessage)
                    .groupChatMessage(groupChatMessage)
                    .emoji(emojiUtf8)
                    .user(user)
                    .createdDate(LocalDateTime.now())
                    .build();
            if(singleChatMessage != null ) singleChatMessage.getReactions().add(reaction);
            if(groupChatMessage != null) groupChatMessage.getReactions().add(reaction);
            message = "successfully created reaction.";
        }

            if(singleChatMessage != null){
                singleChatMessage.getReactions().forEach(
                        reaction1 -> {
                            ReactionDto reactionDto = ReactionDto.builder()
                                    .id(reaction1.getId())
                                    .emoji(reaction1.getEmoji())  .createdDate(dateUtil.formattedDate(reaction1.getCreatedDate()))
                                    .editedDate(reaction1.getEditedDate() == null ? null : dateUtil.formattedDate(reaction1.getEditedDate()))
                                    .userDto(UserDto.builder()
                                            .id(reaction1.getUser().getId())
                                            .firstName(reaction1.getUser().getFirstName())
                                            .lastName(reaction1.getUser().getLastName())
                                            .profileImageUrl(reaction1.getUser().getProfile().getProfileImageName() == null ? null :
                                                    storageService.getImageByName(reaction1.getUser().getProfile().getProfileImageName()))
                                            .build())
                                    .build();

                            reactionDtoList.add(reactionDto);
                        }
                );
            }

        if(singleChatMessage != null){
            SingleChatMessageDto singleChatMessageDto=SingleChatMessageDto.builder()
                    .id(singleChatMessage.getId())
                    .message(singleChatMessage.getMessage())
                    .type(singleChatMessage.getType())
                    .createDate(singleChatMessage.getCreateDate())
                    .userDto(UserDto.builder()
                            .id(singleChatMessage.getUser().getId())
                            .firstName(singleChatMessage.getUser().getFirstName())
                            .lastName(singleChatMessage.getUser().getLastName())
                            .profileImageUrl(singleChatMessage.getUser().getProfile().getProfileImageName()==null?null:storageService.getImageByName(singleChatMessage.getUser().getProfile().getProfileImageName()))
                            .build())
                    .recipientId(singleChatMessage.getRecipientId())
                    .reactionDtoList(reactionDtoList)
                    .isRead(singleChatMessage.isRead())
                    .build();


            if(singleChatMessage.getUser().getId().equals(user.getId())){
                messagingTemplate.convertAndSendToUser(String.valueOf(singleChatMessage.getRecipientId()),"/queue/messages" , singleChatMessageDto);
            }else{
                messagingTemplate.convertAndSendToUser(String.valueOf(singleChatMessage.getUser().getId()),"/queue/messages" , singleChatMessageDto);
            }
            singleChatMessageRepository.save(singleChatMessage);

        }else if (groupChatMessage != null) {
            GroupChatMessageDto groupChatMessageDto =GroupChatMessageDto.builder()
                    .id(groupChatMessage.getId())
                    .text(groupChatMessage.getMessage())
                    .type(groupChatMessage.getType())
                    .createDate(groupChatMessage.getCreateDate())
                    .userDto(UserDto.builder()
                            .id(groupChatMessage.getUser().getId())
                            .firstName(groupChatMessage.getUser().getFirstName())
                            .lastName(groupChatMessage.getUser().getLastName())
                            .profileImageUrl(groupChatMessage.getUser().getProfile().getProfileImageName()==null?null:storageService.getImageByName(singleChatMessage.getUser().getProfile().getProfileImageName()))
                            .build())
                    .reactionDtoList(reactionDtoList)
                    .build();

            messagingTemplate.convertAndSendToUser(String.valueOf(groupChatMessage.getGroupRoom().getId()),"/queue/messages" , groupChatMessageDto);
            groupChatMessageRepository.save(groupChatMessage);
        }


        Response response = Response.builder()
                .statusCode(HttpStatus.OK.value())
                .message(message)
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
