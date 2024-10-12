package com.edusn.Digizenger.Demo.profile.service.impl.aboutImpl.careerHistory;

import com.edusn.Digizenger.Demo.auth.dto.response.Response;
import com.edusn.Digizenger.Demo.auth.entity.User;
import com.edusn.Digizenger.Demo.exception.UserNotFoundException;
import com.edusn.Digizenger.Demo.profile.entity.career_history.Company;
import com.edusn.Digizenger.Demo.profile.repo.CompanyRepository;
import com.edusn.Digizenger.Demo.profile.service.about.CompanyService;
import com.edusn.Digizenger.Demo.utilis.GetUserByRequest;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CompanyServiceImpl implements CompanyService {

    private final CompanyRepository companyRepository;
    private final GetUserByRequest getUserByRequest;

    @Override
    public ResponseEntity<Response> getExistingCompanyNameByName(HttpServletRequest request, String name) {

        User user = getUserByRequest.getUser(request);
        if(user == null) throw new UserNotFoundException("User cannot found");

        List<Company> companies = companyRepository.findCompanyByDynamicName(name);
        List<String> companyNameList = companies.stream().map(
                Company::getCompanyName
        ).collect(Collectors.toList());

        Response response = Response.builder()
                .statusCode(HttpStatus.OK.value())
                .message("successfully got company name by dynamic name.")
                .companyNameList(companyNameList)
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
