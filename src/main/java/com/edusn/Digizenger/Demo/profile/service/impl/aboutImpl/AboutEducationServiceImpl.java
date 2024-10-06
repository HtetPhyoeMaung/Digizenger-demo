//package com.edusn.Digizenger.Demo.profile.service.impl.aboutImpl;
//
//import com.edusn.Digizenger.Demo.auth.dto.response.Response;
//import com.edusn.Digizenger.Demo.profile.repo.EducationRepository;
//import com.edusn.Digizenger.Demo.profile.service.about.AboutEducationService;
//import com.edusn.Digizenger.Demo.storage.StorageService;
//import com.edusn.Digizenger.Demo.utilis.GetUserByRequest;
//import jakarta.servlet.http.HttpServletRequest;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Service;
//
//import java.time.LocalDate;
//
//@Service
//@RequiredArgsConstructor
//public class AboutEducationServiceImpl implements AboutEducationService {
//
//    private final EducationRepository educationRepository;
//    private final GetUserByRequest getUserByRequest;
//    private final StorageService storageService;
//
//    @Override
//    public ResponseEntity<Response> uploadEducation(HttpServletRequest request, String type, String name, String degreeOrDiplomaName, String logoName, LocalDate joinDate, LocalDate endDate, String present) {
//        return null;
//    }
//
//    @Override
//    public ResponseEntity<Response> updateEducation(HttpServletRequest request, Long id, String type, String name, String degreeOrDiplomaName, String logoName, LocalDate joinDate, LocalDate endDate, String present) {
//        return null;
//    }
//
//    @Override
//    public ResponseEntity<Response> removeEducation(HttpServletRequest request, Long id) {
//        return null;
//    }
//}
