package com.edusn.Digizenger.Demo.profile.service.impl.aboutImpl;

import com.edusn.Digizenger.Demo.auth.dto.response.Response;
import com.edusn.Digizenger.Demo.auth.entity.User;
import com.edusn.Digizenger.Demo.profile.entity.EducationHistory;
import com.edusn.Digizenger.Demo.profile.entity.Present;
import com.edusn.Digizenger.Demo.profile.entity.Profile;
import com.edusn.Digizenger.Demo.profile.entity.School;
import com.edusn.Digizenger.Demo.profile.repo.EducationHistoryRepository;
import com.edusn.Digizenger.Demo.profile.repo.ProfileRepository;
import com.edusn.Digizenger.Demo.profile.repo.SchoolRepository;
import com.edusn.Digizenger.Demo.profile.service.about.AboutEducationHistoryService;
import com.edusn.Digizenger.Demo.storage.StorageService;
import com.edusn.Digizenger.Demo.utilis.GetUserByRequest;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
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
                    .LogoImageName(logoImageName)
                    .profiles(profiles)
                    .build();
        }else{
            school = schoolRepository.findBySchoolName(schoolName);
        }


        EducationHistory educationHistory = EducationHistory.builder()
                .school(school)
                .degree(degreeName)
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

//        profileRepository.save(profile);
//        schoolRepository.save(school);
        educationHistoryRepository.save(educationHistory);

        Response response = Response.builder()
                .statusCode(HttpStatus.CREATED.value())
                .message("successfully uploaded education history")
                .build();
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<Response> updateEducationHistory(HttpServletRequest request, Long educationHistoryId, String schoolName, String degreeName, String fieldOfStudy, MultipartFile logoImage, LocalDate joinDate, LocalDate endDate) {
        return null;
    }

    @Override
    public ResponseEntity<Response> removeEducationHistory(HttpServletRequest request, Long educationHistoryId) {
        return null;
    }
}
