package com.edusn.Digizenger.Demo.chat.service.impl;

import com.edusn.Digizenger.Demo.auth.dto.response.Response;
import com.edusn.Digizenger.Demo.auth.entity.User;
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
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReactionServiceImpl implements ReactionService {

    private final ReactionRepository reactionRepository;
    private final GetUserByRequest getUserByRequest;
    private final SingleChatMessageRepository singleChatMessageRepository;
    private final GroupChatMessageRepository groupChatMessageRepository;
    private final DateUtil dateUtil;
    private final StorageService storageService;
    private final SimpMessagingTemplate messagingTemplate;

    @Override
    public ResponseEntity<Response> makeReact(String singleMessageId,
                                              String groupMessageId,
                                              String emojiUtf8,
                                              HttpServletRequest request) {

        User user = getUserByRequest.getUser(request);
        SingleChatMessage singleChatMessage = null;
        if(singleMessageId != null) singleChatMessage =  singleChatMessageRepository.findById(singleMessageId)
                .orElseThrow(() -> new CustomNotFoundException("One to one message cannot found in project"));

        GroupChatMessage groupChatMessage = null;
        if(groupMessageId != null) groupChatMessage = groupChatMessageRepository.findById(groupMessageId)
                .orElseThrow(() -> new CustomNotFoundException("Group message cannot found in server"));

        Reaction reaction = Reaction.builder()
                    .singleChatMessage(singleChatMessage)
                    .groupChatMessage(groupChatMessage)
                    .isReacted(true)
                    .emoji(emojiUtf8)
                    .reactedUser(user)
                    .createdDate(LocalDateTime.now())
                    .build();

        ReactionDto reactionDto = ReactionDto.builder()
                    .id(reaction.getId())
                    .isReacted(reaction.isReacted())
                    .emoji(reaction.getEmoji())
                    .createdDate(dateUtil.formattedDate(reaction.getCreatedDate()))
                    .reactedUser(UserDto.builder()
                            .id(reaction.getReactedUser().getId())
                            .firstName(reaction.getReactedUser().getFirstName())
                            .lastName(reaction.getReactedUser().getLastName())
                            .profileImageUrl(reaction.getReactedUser().getProfile().getProfileImageName() == null ? null :
                                    storageService.getImageByName(reaction.getReactedUser().getProfile().getProfileImageName()))
                            .build())
                    .build();


        if(singleChatMessage != null){
            singleChatMessage.setReactions(List.of(reaction));
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
                    .reactionDtoList(List.of(reactionDto))
                    .build();

            messagingTemplate.convertAndSendToUser(String.valueOf(singleChatMessage.getUser().getId()),"/queue/messages" , singleChatMessageDto);
            singleChatMessageRepository.save(singleChatMessage);
        }

        if(groupChatMessage != null){
            groupChatMessage.setReactions(List.of(reaction));
            GroupChatMessageDto singleChatMessageDto=GroupChatMessageDto.builder()
                    .id(groupChatMessage.getId())
                    .text(groupChatMessage.getMessage())
                    .type(groupChatMessage.getType())
                    .createDate(groupChatMessage.getCreateDate())
                    .userDto(UserDto.builder()
                            .id(groupChatMessage.getUser().getId())
                            .firstName(groupChatMessage.getUser().getFirstName())
                            .lastName(groupChatMessage.getUser().getLastName())
                            .profileImageUrl(groupChatMessage.getUser().getProfile().getProfileImageName()==null?null:storageService.getImageByName(groupChatMessage.getUser().getProfile().getProfileImageName()))
                            .build())
                    .reactionDtoList(List.of(reactionDto))
                    .build();

            messagingTemplate.convertAndSendToUser(String.valueOf(groupChatMessage.getGroupRoom().getId()),"/queue/messages" , singleChatMessageDto);
            groupChatMessageRepository.save(groupChatMessage);
        }

        Response response = Response.builder()
                .statusCode(HttpStatus.OK.value())
                .message("successfully reacted.")
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
