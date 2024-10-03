package com.edusn.Digizenger.Demo.profile.dto.response.myProfile;

import com.edusn.Digizenger.Demo.profile.entity.AcademicType;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.net.URL;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AcademicInstitutionDto {
    private Long id;
    private AcademicType academicType;
    private String name;
    private String logoImageName;
    private URL logoImageUrl;
    private String city;

}
