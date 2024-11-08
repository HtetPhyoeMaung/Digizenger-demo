package com.edusn.Digizenger.Demo.profile.service.impl.follower;

import com.amazonaws.services.kms.model.AlreadyExistsException;
import com.edusn.Digizenger.Demo.auth.dto.response.Response;
import com.edusn.Digizenger.Demo.auth.entity.User;
import com.edusn.Digizenger.Demo.exception.CustomNotFoundException;
import com.edusn.Digizenger.Demo.notification.service.NotificationService;
import com.edusn.Digizenger.Demo.profile.dto.response.otherProfile.RelationShipDto;
import com.edusn.Digizenger.Demo.profile.entity.Profile;
import com.edusn.Digizenger.Demo.profile.entity.RelationshipStatus;
import com.edusn.Digizenger.Demo.profile.repo.ProfileRepository;
import com.edusn.Digizenger.Demo.profile.service.FollowerService;
import com.edusn.Digizenger.Demo.profile.utils.ProfileMapperUtils;
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
import org.springframework.web.server.NotAcceptableStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FollowerServiceImpl implements FollowerService {

    private final GetUserByRequest getUserByRequest;
    private final ProfileRepository profileRepository;
    private final ModelMapper modelMapper;
    private final ProfileMapperUtils profileMapperUtils;
    private final NotificationService notificationService;

    @Override
    public ResponseEntity<Response> followToProfile(HttpServletRequest request, Long toFollowProfileId) {
        User user = getUserByRequest.getUser(request);
//        Profile profile = profileRepository.findByUser(user);

        Profile toFollowUserProfile = profileRepository.findById(toFollowProfileId)
                .orElseThrow(() -> new CustomNotFoundException("profile not exists by id : "+toFollowProfileId));

        if(toFollowUserProfile.getFollowers().contains(user.getProfile()))
            throw new AlreadyExistsException("your already followed this user.");

        if(toFollowUserProfile.equals(user.getProfile()))
            throw new NotAcceptableStatusException("You can't follow to your profile.");

        user.getProfile().getFollowing().add(toFollowUserProfile);
        toFollowUserProfile.getFollowers().add(user.getProfile());

        String message = "You following to "
                +toFollowUserProfile.getUser().getFirstName()
                + " "+toFollowUserProfile.getUser().getLastName();

        RelationshipStatus relationshipStatus = RelationshipStatus.FOLLOWING;
        if(!toFollowUserProfile.getFollowing().contains(user.getProfile())) {
            notificationService.sendFollowNotification(user.getProfile(),toFollowUserProfile);

        }
        else{
            user.getProfile().getNeighbors().add(toFollowUserProfile);
            toFollowUserProfile.getNeighbors().add(user.getProfile());
            message = "You following to "
                    +toFollowUserProfile.getUser().getFirstName()
                    + " "+toFollowUserProfile.getUser().getLastName()
                    + ".And now  You are neighbors.";
            notificationService.sendFollowNotification(user.getProfile(),toFollowUserProfile);
            relationshipStatus = RelationshipStatus.NEIGHBOURS;
        }
        profileRepository.save(user.getProfile());
        profileRepository.save(toFollowUserProfile);

        Response response = Response.builder()
                .statusCode(HttpStatus.OK.value())
                .message(message)
                .relationshipStatus(relationshipStatus)
                .build();
        return new  ResponseEntity<>(response,HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Response> unFollowToProfile(HttpServletRequest request, Long toUnFollowProfileId) {

        User user = getUserByRequest.getUser(request);
        Profile toUnFollowUserProfile = profileRepository.findById(toUnFollowProfileId)
                .orElseThrow(() -> new CustomNotFoundException("profile not exists by id : "+toUnFollowProfileId));

        if(!toUnFollowUserProfile.getFollowers().contains(user.getProfile()))
            throw new NotAcceptableStatusException("you cannot unfollowed this user because you are  not relationship.");

        if(toUnFollowUserProfile.equals(user.getProfile()))
            throw new NotAcceptableStatusException("you can't unfollow your profile.");

        user.getProfile().getFollowing().remove(toUnFollowUserProfile);
        toUnFollowUserProfile.getFollowers().remove(user.getProfile());
        String message = "You unfollowed the profile : "
                +toUnFollowUserProfile.getUser().getFirstName()+" "
                +toUnFollowUserProfile.getUser().getLastName();

        if(toUnFollowUserProfile.getFollowing().contains(user.getProfile())){
            user.getProfile().getNeighbors().remove(toUnFollowUserProfile);
            toUnFollowUserProfile.getNeighbors().remove(user.getProfile());
        }
        profileRepository.save(user.getProfile());
        profileRepository.save(toUnFollowUserProfile);

        Response response = Response.builder()
                .statusCode(HttpStatus.OK.value())
                .message(message)
                .relationshipStatus(RelationshipStatus.FOLLOW)
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Response> getProfileFollowersByPage(int _page, int _limit,Long profileId, HttpServletRequest request) {

        Pageable pageable = PageRequest.of(_page - 1, _limit);
        User user = getUserByRequest.getUser(request);

        if(user.getProfile().getId().equals(profileId)){
            if(user.getProfile().getFollowers().isEmpty())
                throw new CustomNotFoundException("followers are not have in Your profile.");

            Page<Profile> followers = profileRepository.findFollowersByProfileId(user.getProfile().getId(),pageable);
            List<RelationShipDto> profileFollowers = followers.stream().map(
                    profileMapperUtils::convertToRelationShipDto
            ).collect(Collectors.toList());

            Response response = Response.builder()
                    .statusCode(HttpStatus.OK.value())
                    .message("successfully got your profile followers by id")
                    .relationShipDtoList(profileFollowers)
                    .build();
            return new ResponseEntity<>(response, HttpStatus.OK);
        }

        Profile otherUserProfile = profileRepository.findById(profileId)
                .orElseThrow(() -> new CustomNotFoundException("profile not exists by id "+ profileId));

        Page<Profile> followers = profileRepository.findFollowersByProfileId(otherUserProfile.getId(),pageable);
        List<RelationShipDto> otherProfileFollowers = followers.stream().map(
                profileMapperUtils::convertToRelationShipDto
        ).collect(Collectors.toList());

        if(otherProfileFollowers.isEmpty())
            throw new CustomNotFoundException("Followers are not found in "
                    +otherUserProfile.getUser().getFirstName()
                    +" "+otherUserProfile.getUser().getLastName()
                    +"'s profile.");

        Response response = Response.builder()
                .statusCode(HttpStatus.OK.value())
                .message("successfully got "+ otherUserProfile.getUser().getFirstName()
                        +" "+otherUserProfile.getUser().getLastName()
                        +"'s followers.")
                .relationShipDtoList(otherProfileFollowers)
                .build();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
