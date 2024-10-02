package com.edusn.Digizenger.Demo.profile.service.impl.aboutImpl;

import com.edusn.Digizenger.Demo.auth.dto.response.Response;
import com.edusn.Digizenger.Demo.auth.entity.User;
import com.edusn.Digizenger.Demo.profile.entity.Profile;
import com.edusn.Digizenger.Demo.profile.entity.ServiceProvided;
import com.edusn.Digizenger.Demo.profile.repo.ProfileRepository;
import com.edusn.Digizenger.Demo.profile.repo.ServiceProvidedRepository;
import com.edusn.Digizenger.Demo.profile.service.about.AboutProvidedService;
import com.edusn.Digizenger.Demo.utilis.GetUserByRequest;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AboutProvidedServiceImpl implements AboutProvidedService {

    private final ServiceProvidedRepository serviceProvidedRepository;
    private final GetUserByRequest getUserByRequest;
    private final ProfileRepository profileRepository;

    @Override
    public ResponseEntity<Response> findByServiceName(HttpServletRequest servletRequest, String serviceName) {
        return null;
    }

    @Override
    public ResponseEntity<Response> uploadServiceProvided(HttpServletRequest request,
                                                          String service) {
        User user = getUserByRequest.getUser(request);
        Profile profile = profileRepository.findByUser(user);
        List<Profile> profileList = new LinkedList<>();
        profileList.add(profile);

        ServiceProvided serviceProvided = ServiceProvided.builder()
                .service(service)
                .profileList(profileList)
                .build();
        serviceProvidedRepository.save(serviceProvided);

        Response response = Response.builder()
                .statusCode(HttpStatus.OK.value())
                .message("successfully uploaded provided service.")
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Response> updateServiceProvided(HttpServletRequest request,
                                                          Long id,
                                                          String service) {

        User user = getUserByRequest.getUser(request);
        Profile profile = profileRepository.findByUser(user);

        for(ServiceProvided serviceProvided : profile.getServiceProvidedList()){
            if(serviceProvided.getId() == id){
                serviceProvided.setId(id);
                serviceProvided.setService(service);
                serviceProvidedRepository.save(serviceProvided);
            }
        }

        Response response = Response.builder()
                .statusCode(HttpStatus.OK.value())
                .message("successfully updated provided service.")
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Response> removeServiceProvided(HttpServletRequest request,
                                                          Long id) {
        User user = getUserByRequest.getUser(request);
        Profile profile = profileRepository.findByUser(user);

        for(ServiceProvided serviceProvided : profile.getServiceProvidedList()){
            if(serviceProvided.getId() == id){
                profile.getServiceProvidedList().remove(serviceProvided);
                serviceProvidedRepository.delete(serviceProvided);
            }
        }

        Response response = Response.builder()
                .statusCode(HttpStatus.OK.value())
                .message("successfully deleted provided service.")
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
