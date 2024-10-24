package com.edusn.Digizenger.Demo.profile.service.impl.following;

import com.edusn.Digizenger.Demo.auth.dto.response.Response;
import com.edusn.Digizenger.Demo.auth.entity.User;
import com.edusn.Digizenger.Demo.exception.FollowerNotFoundException;
import com.edusn.Digizenger.Demo.exception.FollowingNotFoundException;
import com.edusn.Digizenger.Demo.exception.ProfileNotFoundException;
import com.edusn.Digizenger.Demo.profile.dto.response.otherProfile.RelationShipDto;
import com.edusn.Digizenger.Demo.profile.entity.Profile;
import com.edusn.Digizenger.Demo.profile.repo.ProfileRepository;
import com.edusn.Digizenger.Demo.profile.service.FollowerService;
import com.edusn.Digizenger.Demo.profile.service.FollowingService;
import com.edusn.Digizenger.Demo.profile.utils.ProfileMapperUtils;
import com.edusn.Digizenger.Demo.utilis.GetUserByRequest;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FollowingServiceImpl implements FollowingService {

    private final ProfileRepository profileRepository;
    private final ProfileMapperUtils profileMapperUtils;
    private final GetUserByRequest getUserByRequest;

    @Override
    public ResponseEntity<Response> getProfileFollowingByPage(int _page, int _limit, Long profileId, HttpServletRequest request) {

        Pageable pageable = PageRequest.of(_page - 1, _limit);
        User user = getUserByRequest.getUser(request);
        Profile profile = profileRepository.findByUser(user);

        if(profile.getId().equals(profileId)){

            Page<Profile> followingPage = profileRepository.findFollowingByProfileId(profile.getId(),pageable);
            List<RelationShipDto> profilefollowingList = followingPage.stream().map(
                    following -> profileMapperUtils.convertToRelationShipDto(following)
            ).collect(Collectors.toList());

            if(profilefollowingList.isEmpty())
                throw new FollowingNotFoundException("following are not have in Your profile.");

            Response response = Response.builder()
                    .statusCode(HttpStatus.OK.value())
                    .message("successfully got your profile following by id : "+profileId)
                    .relationShipDtoList(profilefollowingList)
                    .build();
            return new ResponseEntity<>(response, HttpStatus.OK);
        }

        Profile otherUserProfile = profileRepository.findById(profileId)
                .orElseThrow(() -> new ProfileNotFoundException("profile not exists by id: "+profileId));

        Page<Profile> followingPage = profileRepository.findFollowingByProfileId(otherUserProfile.getId(),pageable);
        List<RelationShipDto> followingList = followingPage.stream().map(
                following -> profileMapperUtils.convertToRelationShipDto(following)
        ).collect(Collectors.toList());

        if(followingList.isEmpty())
            throw new FollowingNotFoundException("Following are not found in "
                    +otherUserProfile.getUser().getFirstName()
                    +" "+otherUserProfile.getUser().getLastName()
                    +"'s profile.");

        Response response = Response.builder()
                .statusCode(HttpStatus.OK.value())
                .message("successfully got "+ otherUserProfile.getUser().getFirstName()
                        +" "+otherUserProfile.getUser().getLastName()
                        +"'s following.")
                .relationShipDtoList(followingList)
                .build();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
