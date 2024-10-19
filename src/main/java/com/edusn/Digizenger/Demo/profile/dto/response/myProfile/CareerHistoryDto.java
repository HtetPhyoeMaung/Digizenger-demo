package com.edusn.Digizenger.Demo.profile.dto.response.myProfile;

import com.edusn.Digizenger.Demo.profile.entity.Present;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.net.URL;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CareerHistoryDto {
    private Long id;
    private String designation;
    private LocalDate joinDate;
    private LocalDate endDate;
    private Present present;
    private CompanyDto companyDto;

}
