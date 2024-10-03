package com.edusn.Digizenger.Demo.profile.service.impl.aboutImpl;

import com.edusn.Digizenger.Demo.auth.dto.response.Response;
import com.edusn.Digizenger.Demo.auth.entity.User;
import com.edusn.Digizenger.Demo.exception.CareerHistoryNotFoundException;
import com.edusn.Digizenger.Demo.profile.entity.CareerHistory;
import com.edusn.Digizenger.Demo.profile.entity.Present;
import com.edusn.Digizenger.Demo.profile.entity.Profile;
import com.edusn.Digizenger.Demo.profile.repo.CareerHistoryRepository;
import com.edusn.Digizenger.Demo.profile.repo.ProfileRepository;
import com.edusn.Digizenger.Demo.profile.service.about.AboutCareerHistoryService;
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

@Service
@RequiredArgsConstructor
public class AboutCareerHistoryServiceImpl implements AboutCareerHistoryService {

    private final ProfileRepository profileRepository;
    private final CareerHistoryRepository careerHistoryRepository;
    private final GetUserByRequest getUserByRequest;
    private final StorageService storageService;

    @Override
    public ResponseEntity<Response> uploadCareerHistory(HttpServletRequest request,
                                                        String careerName,
                                                        String companyName,
                                                        MultipartFile companyLogo,
                                                        LocalDate joinDate,
                                                        LocalDate endDate) throws IOException {

        User user = getUserByRequest.getUser(request);
        Profile profile = profileRepository.findByUser(user);
        CareerHistory careerHistory = new CareerHistory();
        careerHistory.setCareerName(careerName);
        careerHistory.setCompanyName(companyName);
        careerHistory.setJoinDate(joinDate);
        careerHistory.setProfile(profile);
        if (companyLogo!=null) {

            String companyLogoName = storageService.uploadImage(companyLogo);

            careerHistory.setCompanyLogoName(companyLogoName);

        }


        if(endDate != null){
            careerHistory.setEndDate(endDate);
            careerHistory.setPresent(Present.NO);
        } else {
            careerHistory.setPresent(Present.YES);
        }
        careerHistoryRepository.save(careerHistory);

        Response response = Response.builder()
                .statusCode(HttpStatus.OK.value())
                .message("successfully uploaded career history.")
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Response> updateCareerHistory(HttpServletRequest request,
                                                        Long id,
                                                        String careerName,
                                                        String companyName,
                                                        MultipartFile companyLogo,
                                                        LocalDate joinDate,
                                                        LocalDate endDate) throws IOException {

        User user = getUserByRequest.getUser(request);
        Profile profile = profileRepository.findByUser(user);



        for(CareerHistory careerHistory : profile.getCareerHistoryList()){
            if(careerHistory.getId().equals(id) ){

                careerHistory.setId(id);
                careerHistory.setCareerName(careerName);
                careerHistory.setCompanyName(companyName);
                if (companyLogo!=null) {
                    String companyLogoName = storageService.uploadImage(companyLogo);
                    careerHistory.setCompanyLogoName(companyLogoName);
                }

                careerHistory.setJoinDate(joinDate);
                if(endDate != null){
                    careerHistory.setEndDate(endDate);
                    careerHistory.setPresent(Present.NO);
                } else {
                careerHistory.setPresent(Present.YES);
                }


                careerHistoryRepository.save(careerHistory);
            }
//            else {
//                throw new CareerHistoryNotFoundException("career history is not found in your profile by id : "+id);
//            }

        }

        Response response = Response.builder()
                .statusCode(HttpStatus.OK.value())
                .message("successfully uploaded career history")
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Response> removeCareerHistoryById(HttpServletRequest request, Long id) {
        User user = getUserByRequest.getUser(request);
        Profile profile = profileRepository.findByUser(user);

        for(CareerHistory careerHistory : profile.getCareerHistoryList()){
                if(careerHistory.getId().equals(id)){
                    profile.getCareerHistoryList().remove(careerHistory);
                    careerHistoryRepository.delete(careerHistory);
                }
//                else {
//                    throw new CareerHistoryNotFoundException("career not found in your profile by id : "+id);
//                }
        }

        Response response = Response.builder()
                .statusCode(HttpStatus.OK.value())
                .message("successfully removed career history.")
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
