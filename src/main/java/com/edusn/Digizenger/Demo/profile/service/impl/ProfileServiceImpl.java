package com.edusn.Digizenger.Demo.profile.service.impl;

import com.edusn.Digizenger.Demo.auth.dto.response.Response;
import com.edusn.Digizenger.Demo.auth.entity.User;
import com.edusn.Digizenger.Demo.exception.ProfileNotFoundException;
import com.edusn.Digizenger.Demo.post.dto.PostDto;
import com.edusn.Digizenger.Demo.post.dto.UserDto;
import com.edusn.Digizenger.Demo.post.repo.LikeRepository;
import com.edusn.Digizenger.Demo.post.repo.PostRepository;
import com.edusn.Digizenger.Demo.post.repo.ViewRepository;
import com.edusn.Digizenger.Demo.post.service.impl.PostServiceImpl;
import com.edusn.Digizenger.Demo.profile.dto.response.myProfile.CareerHistoryDto;
import com.edusn.Digizenger.Demo.profile.dto.response.myProfile.ProfileDto;
import com.edusn.Digizenger.Demo.profile.dto.response.myProfile.ServiceProvidedDto;
import com.edusn.Digizenger.Demo.profile.dto.response.myProfile.UserForProfileDto;
import com.edusn.Digizenger.Demo.profile.entity.Profile;
import com.edusn.Digizenger.Demo.profile.repo.ProfileRepository;
import com.edusn.Digizenger.Demo.profile.service.OtherProfileService;
import com.edusn.Digizenger.Demo.profile.service.ProfileService;
import com.edusn.Digizenger.Demo.storage.StorageService;
import com.edusn.Digizenger.Demo.utilis.GetUserByRequest;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import static com.edusn.Digizenger.Demo.utilis.MapperUtil.convertToUserDto;

@Service
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService {

    private final ProfileRepository profileRepository;
    private final ModelMapper modelMapper;
    private final OtherProfileService otherProfileService;
    private final StorageService storageService;
    private final GetUserByRequest getUserByRequest;
    private final ViewRepository viewRepository;
    private final LikeRepository likeRepository;
    private final PostRepository postRepository;
    @Value("${app.profileUrl}")
    private String baseProfileUrl;

    /** Create profile **/
    @Override
    public void createUserProfile(User user) {

        /* Create profile object */
        Profile profile = new Profile();
        String randomString = UrlGenerator.generateRandomString();
        profile.setProfileLinkUrl(baseProfileUrl+randomString);
        profile.setUser(user);
        profileRepository.save(profile);
    }

    /** Get Logged-in user's Profile **/
    @Override
    public ResponseEntity<Response> showUserProfile(HttpServletRequest request) throws IOException {

        User user = getUserByRequest.getUser(request);
        Profile profile = user.getProfile();

        if(profile.getUsername() != null){
            profile.setProfileLinkUrl(baseProfileUrl+profile.getUsername());
            profileRepository.save(profile);
        }

        ProfileDto existProfileDto = modelMapper.map(profile, ProfileDto.class);
        UserForProfileDto userForProfileDto = modelMapper.map(profile.getUser(), UserForProfileDto.class);

        if(profile.getUser().getPosts() != null){
            List<PostDto> postDtoList = profile.getUser().getPosts().stream().map(post -> {
                Long viewCount = viewRepository.countByPost(post);
                Long likeCount = likeRepository.countByPostAndIsLiked(post, true);
                boolean isLike = post.getLikes().stream()
                        .anyMatch(like -> like.getUser().equals(user) && like.isLiked());
                PostDto postDto = PostServiceImpl.convertToPostDto(post);
                if (post.getUser().getProfile().getProfileImageName() != null) {
                    postDto.getProfileDto().setProfileImageName(post.getUser().getProfile().getProfileImageName());
                    postDto.getProfileDto().setProfileImageUrl(storageService.getImageByName(post.getUser().getProfile().getProfileImageName()));
                }
                postDto.setImageUrl(storageService.getImageByName(post.getImageName()));
                postDto.setViewCount(viewCount);
                postDto.setProfileDto(null);
                postDto.setLikeCount(likeCount);
                postDto.setLiked(isLike);
                return postDto;
            }).collect(Collectors.toList()); // Collect into a List

            userForProfileDto.setPostDtoList(postDtoList);
        }

        existProfileDto.setUserForProfileDto(userForProfileDto);
        if(existProfileDto.getProfileImageName() != null){
            existProfileDto.setProfileImageUrl(
                    storageService.getImageByName(existProfileDto.getProfileImageName())
            );
        }
        if(existProfileDto.getCoverImageName() != null){
            existProfileDto.setCoverImageUrl(
                    storageService.getImageByName(existProfileDto.getCoverImageName())
            );
        }

        /** For CareerHistory **/
        if(!profile.getCareerHistoryList().isEmpty()){
            List<CareerHistoryDto> careerHistoryDtoList = profile.getCareerHistoryList().stream().map(
                    careerHistory -> {
                        CareerHistoryDto careerHistoryDto = modelMapper.map(careerHistory, CareerHistoryDto.class);
                        if(careerHistoryDto.getCompanyLogoName() != null ) {
                            careerHistoryDto.setCompanyLogoUrl(storageService.getImageByName(careerHistoryDto.getCompanyLogoName()));
                        }
                    return careerHistoryDto;
            }).collect(Collectors.toList());
            existProfileDto.setCareerHistoryDtoList(careerHistoryDtoList);
        }

        /** Service Provided **/
        if(!profile.getServiceProvidedList().isEmpty()){
            List<ServiceProvidedDto> serviceProvidedDtoList = profile.getServiceProvidedList().stream().map(
                    serviceProvided -> modelMapper.map(serviceProvided, ServiceProvidedDto.class)
            ).collect(Collectors.toList());
            existProfileDto.setServiceProvidedDtoList(serviceProvidedDtoList);
        }

        /** Service **/
        if(!profile.getFollowers().isEmpty()){
            existProfileDto.setFollowersCount(Long.valueOf(profile.getFollowers().size()));
        }

        /** Following **/
        if(!profile.getFollowing().isEmpty()){
            existProfileDto.setFollowingCount(Long.valueOf(profile.getFollowing().size()));
        }

        /** Neighbors **/
        if(!profile.getNeighbors().isEmpty()){
            existProfileDto.setNeighborCount(Long.valueOf(profile.getNeighbors().size()));
        }

        Response response = Response.builder()
                .statusCode(HttpStatus.OK.value())
                .message("successfully showed existed profile data..")
                .profileDto(existProfileDto)
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Response> getProfileByProfileUrlLink(String profileUrl, HttpServletRequest request) throws IOException {

        User user = getUserByRequest.getUser(request);
        Profile profile = profileRepository.findByUser(user);
        if(profile.getProfileLinkUrl().equals(baseProfileUrl+profileUrl)){
            return showUserProfile(request);
        }
        else {
            Profile otherProfile = profileRepository.findByProfileLinkUrl(baseProfileUrl+profileUrl);
            if(otherProfile == null){throw new ProfileNotFoundException("profile cannot found by url : "+profile.getProfileLinkUrl());}
            return otherProfileService.showOtherUserProfile(otherProfile);
        }
    }
}
