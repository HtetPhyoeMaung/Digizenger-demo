package com.edusn.Digizenger.Demo.profile.service.impl.aboutImpl;

import com.edusn.Digizenger.Demo.auth.dto.response.Response;
import com.edusn.Digizenger.Demo.auth.entity.User;
import com.edusn.Digizenger.Demo.exception.CareerHistoryNotFoundException;
import com.edusn.Digizenger.Demo.profile.dto.response.myProfile.CareerHistoryDto;
import com.edusn.Digizenger.Demo.profile.dto.response.myProfile.ProfileDto;
import com.edusn.Digizenger.Demo.profile.entity.CareerHistory;
import com.edusn.Digizenger.Demo.profile.entity.Present;
import com.edusn.Digizenger.Demo.profile.entity.Profile;
import com.edusn.Digizenger.Demo.profile.repo.CareerHistoryRepository;
import com.edusn.Digizenger.Demo.profile.repo.ProfileRepository;
import com.edusn.Digizenger.Demo.profile.service.about.AboutCareerHistoryService;
import com.edusn.Digizenger.Demo.storage.StorageService;
import com.edusn.Digizenger.Demo.utilis.GetUserByRequest;
import com.edusn.Digizenger.Demo.utilis.UrlConverter;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.w3c.dom.stylesheets.LinkStyle;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AboutCareerHistoryServiceImpl implements AboutCareerHistoryService {

    private final ProfileRepository profileRepository;
    private final CareerHistoryRepository careerHistoryRepository;
    private final GetUserByRequest getUserByRequest;
    private final StorageService storageService;
    private final ModelMapper modelMapper;

    @Override
    public ResponseEntity<Response> uploadCareerHistory(HttpServletRequest request,
                                                        String careerName,
                                                        String companyName,
                                                        MultipartFile companyLogo,
                                                        LocalDate joinDate,
                                                        LocalDate endDate,
                                                        String present) throws IOException {

        User user = getUserByRequest.getUser(request);
        List<Profile> profileList = new ArrayList<>();
        Profile profile = profileRepository.findByUser(user);
        profileList.add(profile);

        String companyLogoName = storageService.uploadImage(companyLogo);
        URL companyLogoUrl = storageService.getImageByName(companyLogoName);

        CareerHistory careerHistory = CareerHistory.builder()
                .careerName(careerName)
                .companyName(companyName)
                .companyLogoName(companyLogoName)
                .companyLogoUrl(companyLogoUrl.toString())
                .joinDate(joinDate)
                .endDate(endDate)
                .present(Present.valueOf(present))
                .profileList(profileList)
                .build();
        careerHistoryRepository.save(careerHistory);
        profileRepository.save(profile);

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
                                                        LocalDate endDate,
                                                        String present) throws IOException {

        User user = getUserByRequest.getUser(request);
        List<Profile> profileList = new ArrayList<>();
        Profile profile = profileRepository.findByUser(user);
        profileList.add(profile);

        String companyLogoName = storageService.uploadImage(companyLogo);
        URL companyLogoUrl = storageService.getImageByName(companyLogoName);

        CareerHistory careerHistory = careerHistoryRepository.findById(id).orElseThrow(
                () -> new CareerHistoryNotFoundException("career not found by  id : " + id)
        );

        CareerHistory updateCareerHistory = CareerHistory.builder()
                .id(id)
                .careerName(careerName)
                .companyName(companyName)
                .companyLogoName(companyLogoName)
                .companyLogoUrl(companyLogoUrl.toString())
                .joinDate(joinDate)
                .endDate(endDate)
                .present(Present.valueOf(present))
                .profileList(profileList)
                .build();
        careerHistoryRepository.save(updateCareerHistory);
        profileRepository.save(profile);

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
        CareerHistory careerHistory = careerHistoryRepository.findById(id).orElseThrow(
                () -> new CareerHistoryNotFoundException("career history cannot found by id : "+id)
        );

        List<CareerHistory> careerHistoryList = profile.getCareerHistoryList();
        for(CareerHistory careerHistoryLoop : careerHistoryList){
            if(careerHistoryLoop.getId() == id){
                careerHistoryRepository.deleteById(id);
            }
        }
        profileRepository.save(profile);

        Response response = Response.builder()
                .statusCode(HttpStatus.OK.value())
                .message("successfully removed career history.")
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
