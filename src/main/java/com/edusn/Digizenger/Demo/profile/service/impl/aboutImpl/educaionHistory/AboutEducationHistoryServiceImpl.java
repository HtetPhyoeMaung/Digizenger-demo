package com.edusn.Digizenger.Demo.profile.service.impl.aboutImpl.educaionHistory;

import com.edusn.Digizenger.Demo.auth.dto.response.Response;
import com.edusn.Digizenger.Demo.auth.entity.User;
import com.edusn.Digizenger.Demo.exception.CustomNotFoundException;
import com.edusn.Digizenger.Demo.profile.dto.response.myProfile.EducationHistoryDto;
import com.edusn.Digizenger.Demo.profile.entity.education_history.EducationHistory;
import com.edusn.Digizenger.Demo.profile.entity.Present;
import com.edusn.Digizenger.Demo.profile.entity.Profile;
import com.edusn.Digizenger.Demo.profile.entity.education_history.School;
import com.edusn.Digizenger.Demo.profile.repo.EducationHistoryRepository;
import com.edusn.Digizenger.Demo.profile.repo.ProfileRepository;
import com.edusn.Digizenger.Demo.profile.repo.SchoolRepository;
import com.edusn.Digizenger.Demo.profile.service.about.AboutEducationHistoryService;
import com.edusn.Digizenger.Demo.storage.StorageService;
import com.edusn.Digizenger.Demo.utilis.GetUserByRequest;
import com.edusn.Digizenger.Demo.utilis.MapperUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.mapper.Mapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AboutEducationHistoryServiceImpl implements AboutEducationHistoryService {

    private final EducationHistoryRepository educationHistoryRepository;
    private final SchoolRepository schoolRepository;
    private final ProfileRepository profileRepository;
    private final GetUserByRequest getUserByRequest;
    private final StorageService storageService;


    @Override
    public ResponseEntity<Response> uploadEducationHistory(HttpServletRequest request,
                                                           String schoolName,
                                                           MultipartFile logoImage,
                                                           String degreeName,
                                                           String fieldOfStudy,
                                                           LocalDate joinDate,
                                                           LocalDate endDate) throws IOException {
        User user = getUserByRequest.getUser(request);
        Profile profile = profileRepository.findByUser(user);
        List<Profile> profiles = new LinkedList<>();
        profiles.add(profile);

        String logoImageName = null;
        if(logoImage != null){
            logoImageName = storageService.uploadImage(logoImage);
        }


        School school;

        if(!schoolRepository.existsBySchoolName(schoolName)){
            school = School.builder()
                    .schoolName(schoolName)
                    .logoImageName(logoImageName)
                    .profiles(profiles)
                    .build();
        }else{
            school = schoolRepository.findBySchoolName(schoolName);
        }


        EducationHistory educationHistory = EducationHistory.builder()
                .school(school)
                .degreeName(degreeName)
                .fieldOfStudy(fieldOfStudy)
                .joinDate(joinDate)
                .profile(profile)
                .build();

        if(endDate != null){
            educationHistory.setEndDate(endDate);
            educationHistory.setPresent(Present.NO);
        } else {
            educationHistory.setPresent(Present.YES);
        }

        List<EducationHistory> educationHistories = new LinkedList<>();
        educationHistories.add(educationHistory);

        List<School> schools = new LinkedList<>();
        schools.add(school);

        school.setEducationHistories(educationHistories);
        profile.setEducationHistories(educationHistories);
        profile.setSchools(schools);

        EducationHistory createdEducationHistory = educationHistoryRepository.save(educationHistory);
        EducationHistoryDto educationHistoryDto = MapperUtil.convertToEducationHistoryDto(createdEducationHistory);

        if(createdEducationHistory.getSchool().getLogoImageName() != null)
            educationHistoryDto.getSchoolDto().setLogoImageUrl(storageService.getImageByName(
                    createdEducationHistory.getSchool().getLogoImageName()
            ));

        Response response = Response.builder()
                .statusCode(HttpStatus.CREATED.value())
                .message("successfully uploaded education history")
                .educationHistoryDto(educationHistoryDto)
                .build();
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<Response> updateEducationHistory(HttpServletRequest request,
                                                           Long educationHistoryId,
                                                           String schoolName,
                                                           String degreeName,
                                                           String fieldOfStudy,
                                                           MultipartFile logoImage,
                                                           LocalDate joinDate,
                                                           LocalDate endDate) throws IOException {
        User user = getUserByRequest.getUser(request);
        Profile profile = profileRepository.findByUser(user);
        List<Profile> profiles = new LinkedList<>();
        profiles.add(profile);

        EducationHistoryDto educationHistoryDto = null;
        for(EducationHistory educationHistory : profile.getEducationHistories()){
            if(educationHistory.getId() == educationHistoryId){

                educationHistory.setId(educationHistoryId);

                String logoImageName = null;
                if(logoImage != null){
                    logoImageName = storageService.uploadImage(logoImage);
                }


                School school;

                if(!schoolRepository.existsBySchoolName(schoolName) && schoolName != null){
                    school = School.builder()
                            .schoolName(schoolName)
                            .logoImageName(logoImageName)
                            .profiles(profiles)
                            .build();
                }else{
                    school = schoolRepository.findBySchoolName(schoolName);
                }

                educationHistory.setSchool(school);

                if(degreeName != null) educationHistory.setDegreeName(degreeName);
                if(fieldOfStudy != null) educationHistory.setFieldOfStudy(fieldOfStudy);
                if(joinDate != null) educationHistory.setJoinDate(joinDate);
                educationHistory.setProfile(profile);


                if(endDate != null){
                    educationHistory.setEndDate(endDate);
                    educationHistory.setPresent(Present.NO);
                } else {
                    educationHistory.setPresent(Present.YES);
                }

                List<EducationHistory> educationHistories = new LinkedList<>();
                educationHistories.add(educationHistory);

                List<School> schools = new LinkedList<>();
                schools.add(school);

                school.setEducationHistories(educationHistories);
                profile.setEducationHistories(educationHistories);
                profile.setSchools(schools);

                EducationHistory updatedEducationHistory = educationHistoryRepository.save(educationHistory);
                educationHistoryDto = MapperUtil.convertToEducationHistoryDto(updatedEducationHistory);

                if(updatedEducationHistory.getSchool().getLogoImageName() != null)
                    educationHistoryDto.getSchoolDto().setLogoImageUrl(storageService.getImageByName(
                            updatedEducationHistory.getSchool().getLogoImageName()
                    ));
            }
        }

        Response response = Response.builder()
                .statusCode(HttpStatus.OK.value())
                .message("successfully updated education history")
                .educationHistoryDto(educationHistoryDto)
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Response> removeEducationHistory(HttpServletRequest request, Long educationHistoryId) {
        User user = getUserByRequest.getUser(request);
        Profile profile = profileRepository.findByUser(user);

        EducationHistory educationHistory = educationHistoryRepository.findById(educationHistoryId)
                        .orElseThrow
                                (() -> new CustomNotFoundException("educationHistory can't exist by id : "+educationHistoryId));

        if(!educationHistory.getProfile().getId().equals(profile.getId()))
            throw new CustomNotFoundException("You cannot remove other's educationHistory.");

        profile.removeEducationHistory(educationHistory);
        School school = schoolRepository.findById(educationHistory.getSchool().getId())
                .orElseThrow(() -> new CustomNotFoundException("school not found"));
        school.removeEducationHistory(educationHistory);
        schoolRepository.save(school);
        profileRepository.save(profile);

        Response response = Response.builder()
                .statusCode(HttpStatus.OK.value())
                .message("successfully deleted.")
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
