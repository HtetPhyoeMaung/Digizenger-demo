package com.edusn.Digizenger.Demo.post.dto;

import com.edusn.Digizenger.Demo.post.entity.Media;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MediaDto {
    private Long id;
    private String mediaUrl;
    private Media.MediaType mediaType;
}
