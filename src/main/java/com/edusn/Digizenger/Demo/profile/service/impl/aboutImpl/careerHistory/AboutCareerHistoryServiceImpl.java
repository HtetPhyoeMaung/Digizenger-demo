package com.edusn.Digizenger.Demo.profile.service.impl.aboutImpl.careerHistory;

import com.edusn.Digizenger.Demo.auth.dto.response.Response;
import com.edusn.Digizenger.Demo.auth.entity.User;
import com.edusn.Digizenger.Demo.auth.repo.UserRepository;
import com.edusn.Digizenger.Demo.exception.CustomNotFoundException;
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
        profile.setCareerHistoryList(careerHistories);
        profile.setCompanies(companies);

        careerHistoryRepository.save(careerHistory);

        Response response = Response.builder()
                .statusCode(HttpStatus.CREATED.value())
                .message("successfully uploaded career history")
                .build();
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<Response> updateCareerHistory(HttpServletRequest request, Long careerHistoryId, String companyName, String designation, MultipartFile logoImage, LocalDate joinDate, LocalDate endDate) throws IOException {

        User user = getUserByRequest.getUser(request);
        Profile profile = profileRepository.findByUser(user);
        List<Profile> profiles = new LinkedList<>();
        profiles.add(profile);

        for(CareerHistory careerHistory : profile.getCareerHistoryList()){
            if(careerHistory.getId() == careerHistoryId){

                careerHistory.setId(careerHistoryId);

                String logoImageName = null;
                if(logoImage != null){
                    logoImageName = storageService.uploadImage(logoImage);
                }


                Company company;

                if(!companyRepository.existsByCompanyName(companyName) && companyName != null){
                    company = Company.builder()
                            .companyName(companyName)
                            .logoImageName(logoImageName)
                            .profiles(profiles)
                            .build();
                }else{
                    company = companyRepository.findByCompanyName(companyName);
                }

                careerHistory.setCompany(company);

                if(designation != null) careerHistory.setDesignation(designation);
                if(joinDate != null) careerHistory.setJoinDate(joinDate);
                careerHistory.setProfile(profile);


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
                profile.setCareerHistoryList(careerHistories);
                profile.setCompanies(companies);

                careerHistoryRepository.save(careerHistory);
            }
        }

        Response response = Response.builder()
                .statusCode(HttpStatus.OK.value())
                .message("successfully updated career history")
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
}
