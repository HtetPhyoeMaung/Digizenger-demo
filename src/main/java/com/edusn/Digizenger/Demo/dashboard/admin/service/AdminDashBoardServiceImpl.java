package com.edusn.Digizenger.Demo.dashboard.admin.service;

import com.edusn.Digizenger.Demo.auth.dto.response.Response;
import com.edusn.Digizenger.Demo.auth.entity.Role;
import com.edusn.Digizenger.Demo.auth.entity.User;
import com.edusn.Digizenger.Demo.auth.repo.UserRepository;
import com.edusn.Digizenger.Demo.dashboard.admin.dto.responeDto.AdminDashBoardDto;
import com.edusn.Digizenger.Demo.dashboard.admin.dto.responeDto.ProfileDtoForDashBoard;
import com.edusn.Digizenger.Demo.dashboard.admin.dto.responeDto.UserDtoForDashBoard;
import com.edusn.Digizenger.Demo.exception.CustomNotFoundException;
import com.edusn.Digizenger.Demo.profile.entity.Profile;
import com.edusn.Digizenger.Demo.profile.repo.ProfileRepository;
import com.edusn.Digizenger.Demo.storage.StorageService;
import com.edusn.Digizenger.Demo.utilis.GetUserByRequest;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AdminDashBoardServiceImpl implements AdminDashBoardService {

    private final UserRepository userRepository;
    private final ProfileRepository profileRepository;
    private final GetUserByRequest getUserByRequest;
    private final StorageService storageService;
    private final ModelMapper modelMapper;


    @Override
    public ResponseEntity<Response> showAdminDashBoard(HttpServletRequest request) {
        User user =  getUserByRequest.getUser(request);
        if(user.getRole().equals(Role.USER.name())) throw new CustomNotFoundException(" Can't  accept by user!");

        /* Profile-Dto*/
        Profile profile = profileRepository.findByUser(user);

        ProfileDtoForDashBoard profileDtoForDashBoard = ProfileDtoForDashBoard.builder()
                .id(profile.getId())
                .profileLinkUrl(profile.getProfileLinkUrl())
                .build();

        if(profile.getProfileImageName() != null){
            profileDtoForDashBoard.setProfileImageUrl(storageService.getImageByName(profile.getProfileImageName()));
        }

        // UserDto //
        UserDtoForDashBoard userDtoForDashBoard = modelMapper.map(user,UserDtoForDashBoard.class);
        userDtoForDashBoard.setProfileDtoForDashBoard(profileDtoForDashBoard);

        // adminDashBoardDto //
        AdminDashBoardDto adminDashBoardDto =new AdminDashBoardDto();
        adminDashBoardDto.setTotalUsers(userRepository.count());

        /* New User **/
        LocalDateTime last30days = LocalDateTime.now().minusDays(30);
        adminDashBoardDto.setNewUsers(userRepository.countByCreatedDateAfter(last30days));


        adminDashBoardDto.setVerifiedUsers(userRepository.countByVerifiedTrue());

        adminDashBoardDto.setUserDtoForDashBoard(userDtoForDashBoard);

        Response response = Response.builder()
                .statusCode(HttpStatus.OK.value())
                .message("Success fully got admin dash board!")
                .adminDashBoardDto(adminDashBoardDto)
                .build();

        return new ResponseEntity<>(response,HttpStatus.OK);
    }
}
