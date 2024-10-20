package com.edusn.Digizenger.Demo.profile.service.impl.aboutImpl.careerHistory;

import com.edusn.Digizenger.Demo.auth.dto.response.Response;
import com.edusn.Digizenger.Demo.auth.entity.User;
import com.edusn.Digizenger.Demo.exception.CustomNotFoundException;
import com.edusn.Digizenger.Demo.profile.dto.response.myProfile.CareerHistoryDto;
import com.edusn.Digizenger.Demo.profile.entity.Present;
import com.edusn.Digizenger.Demo.profile.entity.Profile;
import com.edusn.Digizenger.Demo.profile.entity.career_history.CareerHistory;
import com.edusn.Digizenger.Demo.profile.entity.career_history.Company;
import com.edusn.Digizenger.Demo.profile.repo.CareerHistoryRepository;
import com.edusn.Digizenger.Demo.profile.repo.CompanyRepository;
import com.edusn.Digizenger.Demo.profile.repo.ProfileRepository;
import com.edusn.Digizenger.Demo.profile.service.about.AboutCareerHistoryService;
import com.edusn.Digizenger.Demo.storage.StorageService;
import com.edusn.Digizenger.Demo.utilis.GetUserByRequest;
import com.edusn.Digizenger.Demo.utilis.MapperUtil;
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
public class AboutCareerHistoryServiceImpl implements AboutCareerHistoryService {

    private final CareerHistoryRepository careerHistoryRepository;
    private final CompanyRepository companyRepository;
    private final GetUserByRequest getUserByRequest;
    private final ProfileRepository profileRepository;
    private final StorageService storageService;

    @Override
    public ResponseEntity<Response> uploadCareerHistory(HttpServletRequest request, String companyName, MultipartFile logoImage, String designation, LocalDate joinDate, LocalDate endDate) throws IOException {

        User user = getUserByRequest.getUser(request);
        Profile profile = profileRepository.findByUser(user);
        List<Profile> profiles = new LinkedList<>();
        profiles.add(profile);

        String logoImageName = null;
        if(logoImage != null){
            logoImageName = storageService.uploadImage(logoImage);
        }


        Company company;

        if(!companyRepository.existsByCompanyName(companyName)){
            company = Company.builder()
                    .companyName(companyName)
                    .logoImageName(logoImageName)
                    .profiles(profiles)
                    .build();
        }else{
            company = companyRepository.findByCompanyName(companyName);
        }


        CareerHistory careerHistory = CareerHistory.builder()
                .company(company)
                .designation(designation)
                .joinDate(joinDate)
                .profile(profile)
                .build();

        if(endDate != null){
            careerHistory.setEndDate(endDate);
            careerHistory.setPresent(Present.NO);
        } else {
            careerHistory.setPresent(Present.YES);
        }

        List<CareerHistory> careerHistories = new LinkedList<>();
        careerHistories.add(careerHistory);

        List<Company> companies = new LinkedList<>();
        companies.add(company);

        company.setCareerHistories(careerHistories);
        profile.setCareerHistories(careerHistories);
        profile.setCompanies(companies);

        CareerHistory createdCareerHistory = careerHistoryRepository.save(careerHistory);
        CareerHistoryDto careerHistoryDto = MapperUtil.convertToCareerHistoryDto(createdCareerHistory);

        if(createdCareerHistory.getCompany().getLogoImageName() != null)
            careerHistoryDto.getCompanyDto().setLogoImageUrl(storageService.getImageByName(
                    createdCareerHistory.getCompany().getLogoImageName()
            ));

        Response response = Response.builder()
                .statusCode(HttpStatus.CREATED.value())
                .message("successfully uploaded career history")
                .careerHistoryDto(careerHistoryDto)
                .build();
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<Response> updateCareerHistory(HttpServletRequest request,
                                                        Long careerHistoryId,
                                                        String companyName,
                                                        String designation,
                                                        MultipartFile logoImage,
                                                        LocalDate joinDate,
                                                        LocalDate endDate) throws IOException {

        User user = getUserByRequest.getUser(request);
        Profile profile = profileRepository.findByUser(user);
        List<Profile> profiles = new LinkedList<>();
        profiles.add(profile);

        CareerHistoryDto careerHistoryDto = null;
        for(CareerHistory careerHistory : profile.getCareerHistories()){
            if(careerHistory.getId().equals(careerHistoryId)){

                String logoImageName = null;
                if(logoImage != null){
                    logoImageName = storageService.uploadImage(logoImage);
                }


                Company company;

                if(!companyRepository.existsByCompanyName(companyName)){
                    company = Company.builder()
                            .companyName(companyName)
                            .logoImageName(logoImageName)
                            .profiles(profiles)
                            .build();
                }else{
                    company = companyRepository.findByCompanyName(companyName);
                }


                careerHistory = CareerHistory.builder()
                        .id(careerHistoryId)
                        .company(company)
                        .designation(designation)
                        .joinDate(joinDate)
                        .profile(profile)
                        .build();

                if(endDate != null){
                    careerHistory.setEndDate(endDate);
                    careerHistory.setPresent(Present.NO);
                } else {
                    careerHistory.setPresent(Present.YES);
                }

                List<CareerHistory> careerHistories = new LinkedList<>();
                careerHistories.add(careerHistory);

                List<Company> companies = new LinkedList<>();
                companies.add(company);

                company.setCareerHistories(careerHistories);
                profile.setCareerHistories(careerHistories);
                profile.setCompanies(companies);

                CareerHistory updatedCareerHistory = careerHistoryRepository.save(careerHistory);
                careerHistoryDto = MapperUtil.convertToCareerHistoryDto(updatedCareerHistory);

                if(updatedCareerHistory.getCompany().getLogoImageName() != null)
                    careerHistoryDto.getCompanyDto().setLogoImageUrl(storageService.getImageByName(
                            updatedCareerHistory.getCompany().getLogoImageName()
                    ));
            }
        }
        Response response = Response.builder()
                .statusCode(HttpStatus.OK.value())
                .message("successfully updated career history")
                .careerHistoryDto(careerHistoryDto)
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Response> removeCareerHistory(HttpServletRequest request, Long careerHistoryId) {

        User user = getUserByRequest.getUser(request);
        Profile profile = profileRepository.findByUser(user);

        CareerHistory careerHistory = careerHistoryRepository.findById(careerHistoryId)
                .orElseThrow
                        (() -> new CustomNotFoundException("career history can't exist by id : "+careerHistoryId));

        if(!careerHistory.getProfile().getId().equals(profile.getId()))
            throw new CustomNotFoundException("You cannot remove other's career history.");

        profile.removeCareerHistory(careerHistory);
        Company company = companyRepository.findById(careerHistory.getCompany().getId())
                .orElseThrow(() -> new CustomNotFoundException("company not found"));
        company.removeCareerHistory(careerHistory);
        companyRepository.save(company);
        profileRepository.save(profile);

        Response response = Response.builder()
                .statusCode(HttpStatus.OK.value())
                .message("successfully deleted.")
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Response> careerHistoryGetById(HttpServletRequest request, Long careerHistoryId) {
        User user = getUserByRequest.getUser(request);
        Profile profile = profileRepository.findByUser(user);

        CareerHistoryDto careerHistoryDto = null;
        if(profile.getCareerHistories().isEmpty())
            throw new CustomNotFoundException("career history list cannot found.");
        for(CareerHistory careerHistory: profile.getCareerHistories()){
            if(careerHistory.getId().equals(careerHistoryId)){
                careerHistoryDto = MapperUtil.convertToCareerHistoryDto(careerHistory);
                careerHistoryDto.getCompanyDto().setLogoImageUrl(
                        storageService.getImageByName(careerHistory.getCompany().getLogoImageName())
                );
            }
        }

        if(careerHistoryDto == null) throw new CustomNotFoundException("career history cannot found in you profile");

        Response response = Response.builder()
                .statusCode(HttpStatus.OK.value())
                .message("successfully got career History")
                .careerHistoryDto(careerHistoryDto)
                .build();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
