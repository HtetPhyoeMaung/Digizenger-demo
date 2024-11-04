package com.edusn.Digizenger.Demo.chat.service.impl;

import com.edusn.Digizenger.Demo.auth.dto.response.Response;
import com.edusn.Digizenger.Demo.auth.entity.User;
import com.edusn.Digizenger.Demo.chat.dto.ReactionDto;
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
                .user(user)
                .createdDate(LocalDateTime.now())
                .build();

        ReactionDto reactionDto = ReactionDto.builder()
                .id(reaction.getId())
                .singleMessageId(
                        reaction.getSingleChatMessage() == null ? null : reaction.getSingleChatMessage().getId()
                )
                .groupMessageId(
                        reaction.getGroupChatMessage() == null ? null : reaction.getGroupChatMessage().getId()
                )
                .isReacted(reaction.isReacted())
                .emoji(reaction.getEmoji())
                .createdDate(dateUtil.formattedDate(reaction.getCreatedDate()))
                .userDto(UserDto.builder()
                        .id(reaction.getUser().getId())
                        .firstName(reaction.getUser().getFirstName())
                        .lastName(reaction.getUser().getLastName())
                        .profileImageUrl(reaction.getUser().getProfile().getProfileImageName() == null ? null :
                                storageService.getImageByName(reaction.getUser().getProfile().getProfileImageName()))
                        .build())
                .build();



        Response response = Response.builder()
                .statusCode(HttpStatus.OK.value())
                .message("successfully reacted.")
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
