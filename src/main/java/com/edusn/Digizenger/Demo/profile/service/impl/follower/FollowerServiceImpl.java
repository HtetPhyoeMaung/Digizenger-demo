package com.edusn.Digizenger.Demo.profile.service.impl.follower;

import com.edusn.Digizenger.Demo.auth.dto.response.Response;
import com.edusn.Digizenger.Demo.auth.entity.User;
import com.edusn.Digizenger.Demo.exception.ProfileNotFoundException;
import com.edusn.Digizenger.Demo.profile.entity.Profile;
import com.edusn.Digizenger.Demo.profile.repo.ProfileRepository;
import com.edusn.Digizenger.Demo.profile.service.FollowerService;
import com.edusn.Digizenger.Demo.utilis.GetUserByRequest;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FollowerServiceImpl implements FollowerService {

    private final GetUserByRequest getUserByRequest;
    private final ProfileRepository profileRepository;

    @Override
    public ResponseEntity<Response> followToProfile(HttpServletRequest request, Long toFollowUserProfileId) {
        User user = getUserByRequest.getUser(request);
        Profile profile = profileRepository.findByUser(user);

        Profile toFollowUserProfile = profileRepository.findById(toFollowUserProfileId)
                .orElseThrow(() -> new ProfileNotFoundException("user profile can't found by id : "+toFollowUserProfileId));
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
    public ResponseEntity<Response> unFollowToProfile(HttpServletRequest request, Long toUnFollowUserProfileId) {

        User user = getUserByRequest.getUser(request);
        Profile profile = profileRepository.findByUser(user);
        Profile toUnFollowUserProfile = profileRepository.findById(toUnFollowUserProfileId)
                .orElseThrow(() -> new ProfileNotFoundException("profile can't found by id : "+toUnFollowUserProfileId));

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
    public ResponseEntity<Response> getProfileFollowers(int _page, int _limit, HttpServletRequest request, String profileURL) {
        return null;
    }
}
