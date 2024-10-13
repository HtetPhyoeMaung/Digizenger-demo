package com.edusn.Digizenger.Demo.profile.dto.response.myProfile;

import com.edusn.Digizenger.Demo.profile.entity.Present;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EducationHistoryDto {
    private Long id;
    private String degree;
    private String  fieldOfStudy;
    private LocalDate joinDate;
    private LocalDate endDate;
    private Present present;
    private SchoolDto schoolDto;
}
