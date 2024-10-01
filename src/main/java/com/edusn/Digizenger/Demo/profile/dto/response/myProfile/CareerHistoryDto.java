package com.edusn.Digizenger.Demo.profile.dto.response.myProfile;

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
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CareerHistoryDto {
    private Long id;
    private String careerName;
    private String companyName;
    private LocalDate joinDate;
    private LocalDate endDate;
    private String companyLogoName;
}
