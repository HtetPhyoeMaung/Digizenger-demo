package com.edusn.Digizenger.Demo.post.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LikeDto {
    private Boolean isLike;

    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;
}
