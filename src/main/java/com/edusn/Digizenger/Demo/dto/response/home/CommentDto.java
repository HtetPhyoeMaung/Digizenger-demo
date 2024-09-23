package com.edusn.Digizenger.Demo.dto.response.home;



import com.edusn.Digizenger.Demo.entity.auth.User;
import com.edusn.Digizenger.Demo.entity.post.Reply;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentDto {
    private Long id;
    private String comment;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;
    private User user;
    private List<ReplyDto> replies;
}
