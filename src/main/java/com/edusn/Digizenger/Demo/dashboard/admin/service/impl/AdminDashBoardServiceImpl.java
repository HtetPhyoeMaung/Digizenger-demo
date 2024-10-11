package com.edusn.Digizenger.Demo.dashboard.admin.service.impl;

import com.edusn.Digizenger.Demo.auth.dto.response.Response;
import com.edusn.Digizenger.Demo.auth.entity.Role;
import com.edusn.Digizenger.Demo.auth.entity.User;
import com.edusn.Digizenger.Demo.auth.repo.UserRepository;
import com.edusn.Digizenger.Demo.dashboard.admin.dto.responeDto.AdminDashBoardDto;
import com.edusn.Digizenger.Demo.dashboard.admin.dto.responeDto.ProfileDtoForDashBoard;
import com.edusn.Digizenger.Demo.dashboard.admin.dto.responeDto.UserDtoForDashBoard;
import com.edusn.Digizenger.Demo.dashboard.admin.dto.responeDto.showUser.ProfileDataDto;
import com.edusn.Digizenger.Demo.dashboard.admin.dto.responeDto.showUser.UserDataDto;
import com.edusn.Digizenger.Demo.dashboard.admin.service.AdminDashBoardService;
import com.edusn.Digizenger.Demo.exception.CustomNotFoundException;
import com.edusn.Digizenger.Demo.exception.UserNotFoundException;
import com.edusn.Digizenger.Demo.profile.dto.response.myProfile.CareerHistoryDto;
import com.edusn.Digizenger.Demo.profile.dto.response.myProfile.ServiceProvidedDto;
import com.edusn.Digizenger.Demo.profile.entity.Profile;
import com.edusn.Digizenger.Demo.profile.repo.ProfileRepository;
import com.edusn.Digizenger.Demo.storage.StorageService;
import com.edusn.Digizenger.Demo.utilis.GetUserByRequest;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminDashBoardServiceImpl implements AdminDashBoardService {

    private final UserRepository userRepository;
    private final ProfileRepository profileRepository;
    private final GetUserByRequest getUserByRequest;
    private final StorageService storageService;
    private final ModelMapper modelMapper;


    @Override
    public ResponseEntity<Response> showAdminDashBoard(HttpServletRequest request,int _page, int _limit) {
        User user =  getUserByRequest.getUser(request);
        if(user.getRole().equals(Role.USER.name())) throw new CustomNotFoundException(" Can't  accept by user!");

        Pageable pageable = PageRequest.of(_page - 1, _limit);

        Page<User> userList = userRepository.findAll(pageable);
        List<UserDtoForDashBoard> userDtoForDashBoardList = userList.stream().map(
                userPage -> {
                    // UserDto //
                    UserDtoForDashBoard userDtoForDashBoard = modelMapper.map(userPage,UserDtoForDashBoard.class);
                    userDtoForDashBoard.setCountry(userPage.getAddress().getCountry());
                    userDtoForDashBoard.setVerified(userPage.getVerified());
                    /* Profile-Dto*/
                    Profile profile = profileRepository.findByUser(userPage);

                    ProfileDtoForDashBoard profileDtoForDashBoard = ProfileDtoForDashBoard.builder()
                            .id(profile.getId())
                            .profileLinkUrl(profile.getProfileLinkUrl())
                            .build();

                    if(profile.getProfileImageName() != null) {
                        profileDtoForDashBoard.setProfileImageUrl(storageService.getImageByName(profile.getProfileImageName()));
                    }

                    userDtoForDashBoard.setProfileDtoForDashBoard(profileDtoForDashBoard);

                    return userDtoForDashBoard;

                }
        ).toList();

        // adminDashBoardDto //
        AdminDashBoardDto adminDashBoardDto =new AdminDashBoardDto();

        /* Total Users **/
        adminDashBoardDto.setTotalUsers(userRepository.count());

        /* New Users **/
        LocalDateTime last30days = LocalDateTime.now().minusDays(30);
        adminDashBoardDto.setNewUsers(userRepository.countByCreatedDateAfter(last30days));

        /* Verified Users **/
        adminDashBoardDto.setVerifiedUsers(userRepository.countByVerifiedTrue());

        adminDashBoardDto.setUserDtoForDashBoard(userDtoForDashBoardList);

        Response response = Response.builder()
                .statusCode(HttpStatus.OK.value())
                .message("Success fully got admin dash board!")
                .adminDashBoardDto(adminDashBoardDto)
                .build();

        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    /* To show user's profile by admin */

    @Override
    public ResponseEntity<Response> showUserData(HttpServletRequest request, Long id) {
        User user = getUserByRequest.getUser(request);
        if(user.getRole().equals(Role.USER.name())) throw new CustomNotFoundException("Can't accepted by a user");

        User showUser = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User cannot found by id : "+id));


        UserDataDto userDataDto = modelMapper.map(showUser, UserDataDto.class);

        ProfileDataDto profileDataDto = modelMapper.map(showUser.getProfile(), ProfileDataDto.class);

        /* If profile image and cover image have, set this image url */
        profileDataDto.setProfileImageUrl(
                showUser.getProfile().getProfileImageName() != null
                        ? storageService.getImageByName(showUser.getProfile().getProfileImageName())
                        : null
        );

        profileDataDto.setCoverImageUrl(
                showUser.getProfile().getCoverImageName() != null
                        ? storageService.getImageByName(showUser.getProfile().getCoverImageName())
                        : null

        );

        profileDataDto.setPostCount((long) showUser.getPosts().size());
        profileDataDto.setFollowerCount((long) showUser.getProfile().getFollowers().size());
        profileDataDto.setFollowingCount((long) showUser.getProfile().getFollowing().size());
        profileDataDto.setNeighborCount((long) showUser.getProfile().getNeighbors().size());


        /* careerHistory */
        List<CareerHistoryDto> careerHistoryDtoList = showUser.getProfile().getCareerHistoryList()
                .stream().map(
                        careerHistory -> modelMapper.map(careerHistory, CareerHistoryDto.class)
                ).collect(Collectors.toList());

        /* serviceProvided */
        List<ServiceProvidedDto> serviceProvidedDtoList = showUser.getProfile().getServiceProvidedList()
                .stream().map(
                        serviceProvided -> modelMapper.map(serviceProvided, ServiceProvidedDto.class)
                ).collect(Collectors.toList());

        /* Now Save to profileDataDto */
        profileDataDto.setCareerHistoryList(careerHistoryDtoList);
        profileDataDto.setServiceProvidedList(serviceProvidedDtoList);

        /* profile data must be save to the user data dto */
        userDataDto.setProfileDataDto(profileDataDto);

        Response response = Response.builder()
                .statusCode(HttpStatus.OK.value())
                .message("successfully got user's data by :" + user.getRole())
                .userDataDto(userDataDto)
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
