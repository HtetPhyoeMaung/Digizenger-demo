package com.edusn.Digizenger.Demo.profile.dto.response.myProfile;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ImageDto {
    private Long totalImage = 0L;
    private List<String> imageUrlList;
}
