package com.edusn.Digizenger.Demo.dto.response.home;

import com.edusn.Digizenger.Demo.entity.auth.User;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LikeDto {

    private Long id;
    private boolean liked;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;
    private User user;
}
