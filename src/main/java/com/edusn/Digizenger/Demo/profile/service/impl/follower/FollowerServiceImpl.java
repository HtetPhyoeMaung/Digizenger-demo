package com.edusn.Digizenger.Demo.profile.service.impl.follower;

import com.edusn.Digizenger.Demo.auth.dto.response.Response;
import com.edusn.Digizenger.Demo.auth.entity.User;
import com.edusn.Digizenger.Demo.exception.CannotFollowException;
import com.edusn.Digizenger.Demo.exception.CannotUnfollowException;
import com.edusn.Digizenger.Demo.exception.FollowerNotFoundException;
import com.edusn.Digizenger.Demo.exception.ProfileNotFoundException;
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
        Profile profile = profileRepository.findByUser(user);

        Profile toFollowUserProfile = profileRepository.findById(toFollowProfileId)
                .orElseThrow(() -> new ProfileNotFoundException("profile not exists by id : "+toFollowProfileId));

        if(toFollowUserProfile.getFollowers().contains(profile))
            throw new CannotFollowException("your already followed this user.");

        if(toFollowUserProfile.equals(profile))
            throw new CannotFollowException("You can't follow to your profile.");

        profile.getFollowing().add(toFollowUserProfile);
        toFollowUserProfile.getFollowers().add(profile);

        String message = "You following to "
                +toFollowUserProfile.getUser().getFirstName()
                + " "+toFollowUserProfile.getUser().getLastName();

        RelationshipStatus relationshipStatus = RelationshipStatus.FOLLOWING;
        if(!toFollowUserProfile.getFollowing().contains(profile)) {
            notificationService.sendFollowNotification(profile,toFollowUserProfile);

        }
        else{
            profile.getNeighbors().add(toFollowUserProfile);
            toFollowUserProfile.getNeighbors().add(profile);
            message = "You following to "
                    +toFollowUserProfile.getUser().getFirstName()
                    + " "+toFollowUserProfile.getUser().getLastName()
                    + ".And now  You are neighbors.";
            notificationService.sendFollowNotification(profile,toFollowUserProfile);
            relationshipStatus = RelationshipStatus.NEIGHBOURS;
        }
        profileRepository.save(profile);
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
        Profile profile = profileRepository.findByUser(user);
        Profile toUnFollowUserProfile = profileRepository.findById(toUnFollowProfileId)
                .orElseThrow(() -> new ProfileNotFoundException("profile not exists by id : "+toUnFollowProfileId));

        if(!toUnFollowUserProfile.getFollowers().contains(profile))
            throw new CannotUnfollowException("you cannot unfollowed this user because you are  not relationship.");

        if(toUnFollowUserProfile.equals(profile))
            throw new CannotUnfollowException("you can't unfollow your profile.");

        profile.getFollowing().remove(toUnFollowUserProfile);
        toUnFollowUserProfile.getFollowers().remove(profile);
        String message = "You unfollowed the profile : "
                +toUnFollowUserProfile.getUser().getFirstName()+" "
                +toUnFollowUserProfile.getUser().getLastName();

        if(toUnFollowUserProfile.getFollowing().contains(profile)){
            profile.getNeighbors().remove(toUnFollowUserProfile);
            toUnFollowUserProfile.getNeighbors().remove(profile);
        }
        profileRepository.save(profile);
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
        Profile profile = profileRepository.findByUser(user);

        if(profile.getId().equals(profileId)){
            if(profile.getFollowers().isEmpty())
                throw new FollowerNotFoundException("followers are not have in Your profile.");

            Page<Profile> followers = profileRepository.findFollowersByProfileId(profile.getId(),pageable);
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
                .orElseThrow(() -> new ProfileNotFoundException("profile not exists by id "+ profileId));

        Page<Profile> followers = profileRepository.findFollowersByProfileId(otherUserProfile.getId(),pageable);
        List<RelationShipDto> otherProfileFollowers = followers.stream().map(
                profileMapperUtils::convertToRelationShipDto
        ).collect(Collectors.toList());

        if(otherProfileFollowers.isEmpty())
            throw new FollowerNotFoundException("Followers are not found in "
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
