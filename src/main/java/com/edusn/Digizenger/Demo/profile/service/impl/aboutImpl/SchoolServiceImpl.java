package com.edusn.Digizenger.Demo.profile.service.impl.aboutImpl;

import com.edusn.Digizenger.Demo.auth.dto.response.Response;
import com.edusn.Digizenger.Demo.auth.entity.User;
import com.edusn.Digizenger.Demo.exception.UserNotFoundException;
import com.edusn.Digizenger.Demo.profile.entity.School;
import com.edusn.Digizenger.Demo.profile.repo.SchoolRepository;
import com.edusn.Digizenger.Demo.profile.service.about.SchoolService;
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
public class SchoolServiceImpl implements SchoolService {

    private final SchoolRepository schoolRepository;
    private final GetUserByRequest getUserByRequest;

    @Override
    public ResponseEntity<Response> getExistingSchoolNameByName(HttpServletRequest request, String name) {

        User user = getUserByRequest.getUser(request);
        if(user == null) throw new UserNotFoundException("User cannot found");

        List<School> schoolList = schoolRepository.findSchoolByDynamicName(name);
        List<String> schoolNameList = schoolList.stream().map(
                school -> school.getSchoolName()
        ).collect(Collectors.toList());

        Response response = Response.builder()
                .statusCode(HttpStatus.OK.value())
                .message("successfully got school name by dynamic school name.")
                .schoolNameList(schoolNameList)
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
