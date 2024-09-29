package com.edusn.Digizenger.Demo.profile.dto.response.myProfile;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AboutDto {
    private Long id;
    private List<CareerHistoryDto> careerHistoryDtoList;
    private List<EducationDto> educationDtoList;
    private Set<String> servicesProvide;
}
