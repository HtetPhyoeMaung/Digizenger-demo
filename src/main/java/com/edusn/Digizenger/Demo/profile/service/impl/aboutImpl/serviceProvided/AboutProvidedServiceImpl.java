package com.edusn.Digizenger.Demo.profile.service.impl.aboutImpl.serviceProvided;

import com.edusn.Digizenger.Demo.auth.dto.response.Response;
import com.edusn.Digizenger.Demo.auth.entity.User;
import com.edusn.Digizenger.Demo.exception.CustomNotFoundException;
import com.edusn.Digizenger.Demo.profile.dto.response.myProfile.ServiceProvidedDto;
import com.edusn.Digizenger.Demo.profile.entity.Profile;
import com.edusn.Digizenger.Demo.profile.entity.serviceProvided.ServiceProvided;
import com.edusn.Digizenger.Demo.profile.repo.ProfileRepository;
import com.edusn.Digizenger.Demo.profile.repo.ServiceProvidedRepository;
import com.edusn.Digizenger.Demo.profile.service.about.AboutProvidedService;
import com.edusn.Digizenger.Demo.utilis.GetUserByRequest;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AboutProvidedServiceImpl implements AboutProvidedService {

    private final ServiceProvidedRepository serviceProvidedRepository;
    private final GetUserByRequest getUserByRequest;
    private final ProfileRepository profileRepository;
    private final ModelMapper modelMapper;

    @Override
    public ResponseEntity<Response> findByServiceName(HttpServletRequest request, String serviceName) {

        User user = getUserByRequest.getUser(request);
        if(user.getProfile() == null) throw  new CustomNotFoundException("profile Not found Please login first");

        List<ServiceProvided> serviceProvidedList = serviceProvidedRepository.findByServiceNameDynamic(serviceName);
        List<ServiceProvidedDto> serviceProvidedDtoList = serviceProvidedList.stream().map(
                serviceProvided -> modelMapper.map(serviceProvided, ServiceProvidedDto.class)
        ).collect(Collectors.toList());

        Response response = Response.builder()
                .statusCode(HttpStatus.OK.value())
                .message("successfully found by service : "+serviceName)
                .serviceProvidedDtoList(serviceProvidedDtoList)
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Response> uploadServiceProvided(HttpServletRequest request,
                                                          String service){
        User user = getUserByRequest.getUser(request);
        List<Profile> profileList = new LinkedList<>();
        profileList.add(user.getProfile());

        ServiceProvided serviceProvided = ServiceProvided.builder()
                .service(service)
                .profileList(profileList)
                .build();
        serviceProvided = serviceProvidedRepository.save(serviceProvided);

        user.getProfile().addServiceProvided(serviceProvided);
        profileRepository.save(user.getProfile());
        ServiceProvidedDto serviceProvidedDto = ServiceProvidedDto.builder()
                .id(serviceProvided.getId())
                .service(serviceProvided.getService())
                .build();

        Response response = Response.builder()
                .statusCode(HttpStatus.OK.value())
                .message("successfully uploaded provided service.")
                .serviceProvidedDto(serviceProvidedDto)
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Response> uploadServiceProvidedById(HttpServletRequest request,
                                                              Long id) {
        User user = getUserByRequest.getUser(request);

        ServiceProvided serviceProvided = serviceProvidedRepository.findById(id)
                .orElseThrow(() -> new CustomNotFoundException("service provided not found by id : "+id));

        user.getProfile().addServiceProvided(serviceProvided);
        profileRepository.save(user.getProfile());

        Response response = Response.builder()
                .statusCode(HttpStatus.OK.value())
                .message("successfully uploaded service provided.")
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Response> updateServiceProvided(HttpServletRequest request,
                                                          Long id,
                                                          String service) {

        User user = getUserByRequest.getUser(request);

        ServiceProvidedDto serviceProvidedDto = null;
        for(ServiceProvided serviceProvided : user.getProfile().getServiceProvidedList()){
            if(serviceProvided.getId().equals(id)){
//                serviceProvided.setId(id);
                serviceProvided.setService(service);
//                serviceProvided = serviceProvidedRepository.save(serviceProvided);
                profileRepository.save(user.getProfile());

                serviceProvidedDto = ServiceProvidedDto.builder()
                        .id(serviceProvided.getId())
                        .service(serviceProvided.getService())
                        .build();
            }
        }

        Response response = Response.builder()
                .statusCode(HttpStatus.OK.value())
                .message("successfully updated provided service.")
                .serviceProvidedDto(serviceProvidedDto)
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Response> removeServiceProvided(HttpServletRequest request,
                                                          Long id) {
        User user = getUserByRequest.getUser(request);
        ServiceProvided serviceProvided = serviceProvidedRepository.findById(id)
                .orElseThrow(() -> new CustomNotFoundException("service provided not found by id : "+ id));

        user.getProfile().removeProvided(serviceProvided);
        profileRepository.save(user.getProfile());

        Response response = Response.builder()
                .statusCode(HttpStatus.OK.value())
                .message("successfully deleted provided service.")
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
