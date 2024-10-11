package com.edusn.Digizenger.Demo.profile.service.impl.follower;

import com.edusn.Digizenger.Demo.auth.dto.response.Response;
import com.edusn.Digizenger.Demo.auth.entity.User;
import com.edusn.Digizenger.Demo.exception.CannotFollowException;
import com.edusn.Digizenger.Demo.exception.CannotUnfollowException;
import com.edusn.Digizenger.Demo.exception.FollowerNotFoundException;
import com.edusn.Digizenger.Demo.exception.ProfileNotFoundException;
import com.edusn.Digizenger.Demo.profile.dto.response.otherProfile.OtherProfileDto;
import com.edusn.Digizenger.Demo.profile.dto.response.otherProfile.RelationShipDto;
import com.edusn.Digizenger.Demo.profile.entity.Profile;
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

    @Override
    public ResponseEntity<Response> followToProfile(HttpServletRequest request, String toFollowUserProfileUrl) {
        User user = getUserByRequest.getUser(request);
        Profile profile = profileRepository.findByUser(user);

        Profile toFollowUserProfile = profileRepository.findByProfileLinkUrl(toFollowUserProfileUrl);
        if(toFollowUserProfile == null)
             throw  new ProfileNotFoundException("user profile can't found by id : "+toFollowUserProfileUrl);

        if(toFollowUserProfile.equals(profile))
            throw new CannotFollowException("You can't follow to your profile.");

        profile.getFollowing().add(toFollowUserProfile);
        toFollowUserProfile.getFollowers().add(profile);

        String message = "You following to "
                +toFollowUserProfile.getUser().getFirstName()
                + " "+toFollowUserProfile.getUser().getLastName();
        if(toFollowUserProfile.getFollowing().contains(profile)){
            profile.getNeighbors().add(toFollowUserProfile);
            toFollowUserProfile.getNeighbors().add(profile);
            message = "You following to "
                    +toFollowUserProfile.getUser().getFirstName()
                    + " "+toFollowUserProfile.getUser().getLastName()
                    + ".And now  You are neighbors.";
        }
        profileRepository.save(profile);
        profileRepository.save(toFollowUserProfile);

        Response response = Response.builder()
                .statusCode(HttpStatus.OK.value())
                .message(message)
                .build();
        return new  ResponseEntity<>(response,HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Response> unFollowToProfile(HttpServletRequest request, String toUnFollowUserProfileUrl) {

        User user = getUserByRequest.getUser(request);
        Profile profile = profileRepository.findByUser(user);
        Profile toUnFollowUserProfile = profileRepository.findByProfileLinkUrl(toUnFollowUserProfileUrl);

        if(toUnFollowUserProfile == null)
            throw  new ProfileNotFoundException("profile can't found by url : "+toUnFollowUserProfileUrl);
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
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Response> getProfileFollowersByPage(int _page, int _limit,String profileUrl, HttpServletRequest request) {

        Pageable pageable = PageRequest.of(_page - 1, _limit);
        User user = getUserByRequest.getUser(request);
        Profile profile = profileRepository.findByUser(user);

        if(profile.getProfileLinkUrl() == profileUrl){
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

        Profile otherUserProfile = profileRepository.findByProfileLinkUrl(profileUrl);
        if(otherUserProfile.getFollowers() == null)
            throw new FollowerNotFoundException("Followers are not found in "
                    +otherUserProfile.getUser().getFirstName()
                    +" "+otherUserProfile.getUser().getLastName()
                    +"'s profile.");
        Page<Profile> followers = profileRepository.findFollowersByProfileId(otherUserProfile.getId(),pageable);
        List<RelationShipDto> otherProfileFollowers = followers.stream().map(
                profileMapperUtils::convertToRelationShipDto
        ).collect(Collectors.toList());

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
