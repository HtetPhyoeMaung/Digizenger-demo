package com.edusn.Digizenger.Demo.profile.dto.response.myProfile;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CompanyDto {
    private Long id;
    private String companyName;
    private String logoImageUrl;
}
