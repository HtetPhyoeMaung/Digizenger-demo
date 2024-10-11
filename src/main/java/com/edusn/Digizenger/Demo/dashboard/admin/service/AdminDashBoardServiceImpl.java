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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

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
                    if(userDtoForDashBoard.getVerified() == null){
                        userDtoForDashBoard.setVerified(false);
                    }
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
}
