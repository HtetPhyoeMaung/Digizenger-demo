package com.edusn.Digizenger.Demo.dto.response.home;

import com.edusn.Digizenger.Demo.entity.post.Media;
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
