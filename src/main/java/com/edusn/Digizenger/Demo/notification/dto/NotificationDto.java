package com.edusn.Digizenger.Demo.notification.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationDto {
    private Long id;
    private String message;
    private LocalDateTime createDate;
    private boolean isRead;
    private String profileUrl;
    private Long postId;
    private Long userId;
}
