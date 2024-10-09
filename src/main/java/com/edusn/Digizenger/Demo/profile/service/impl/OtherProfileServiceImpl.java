package com.edusn.Digizenger.Demo.profile.service.impl;

import com.edusn.Digizenger.Demo.auth.dto.response.Response;
import com.edusn.Digizenger.Demo.auth.entity.User;
import com.edusn.Digizenger.Demo.post.dto.PostDto;
import com.edusn.Digizenger.Demo.post.dto.UserDto;
import com.edusn.Digizenger.Demo.post.repo.LikeRepository;
import com.edusn.Digizenger.Demo.post.repo.ViewRepository;
import com.edusn.Digizenger.Demo.post.service.impl.PostServiceImpl;
import com.edusn.Digizenger.Demo.profile.dto.response.myProfile.CareerHistoryDto;
import com.edusn.Digizenger.Demo.profile.dto.response.myProfile.ServiceProvidedDto;
import com.edusn.Digizenger.Demo.profile.dto.response.otherProfile.OtherProfileDto;
import com.edusn.Digizenger.Demo.profile.dto.response.otherProfile.OtherUserForProfileDto;
import com.edusn.Digizenger.Demo.profile.entity.Profile;
import com.edusn.Digizenger.Demo.profile.service.OtherProfileService;
import com.edusn.Digizenger.Demo.storage.StorageService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


import java.net.URL;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.edusn.Digizenger.Demo.utilis.MapperUtil.convertToUserDto;

@Service
@RequiredArgsConstructor
public class OtherProfileServiceImpl implements OtherProfileService {

    private final ModelMapper modelMapper;
    private final StorageService storageService;
    private final ViewRepository viewRepository;
    private final LikeRepository likeRepository;

    @Override
    public ResponseEntity<Response> showOtherUserProfile(Profile otherProfile)  {

        User otherUser = otherProfile.getUser();
        OtherUserForProfileDto otherUserForProfileDto = modelMapper.map(otherUser, OtherUserForProfileDto.class);
        if(otherUser.getPosts() != null){
            List<PostDto> postDtoList = otherProfile.getUser().getPosts().stream().map(post -> {
                Long viewCount = viewRepository.countByPost(post);
                Long likeCount = likeRepository.countByPostAndIsLiked(post, true);
                boolean isLike = post.getLikes().stream()
                        .anyMatch(like -> like.getUser().equals(otherUser) && like.isLiked());
                PostDto postDto = PostServiceImpl.convertToPostDto(post);
                if (post.getUser().getProfile().getProfileImageName() != null) {
                    postDto.getProfileDto().setProfileImageName(post.getUser().getProfile().getProfileImageName());
                    postDto.getProfileDto().setProfileImageUrl(storageService.getImageByName(post.getUser().getProfile().getProfileImageName()));
                }
                postDto.setImageUrl(storageService.getImageByName(post.getImageName()));
                postDto.setProfileDto(null);
                postDto.setViewCount(viewCount);
                postDto.setLikeCount(likeCount);
                postDto.setLiked(isLike);
                return postDto;
            }).collect(Collectors.toList()); // Collect into a List

            otherUserForProfileDto.setPostDtoList(postDtoList);
        }
        OtherProfileDto otherProfileDto = modelMapper.map(otherProfile, OtherProfileDto.class);
        otherProfileDto.setOtherUserForProfileDto(otherUserForProfileDto);

        if(otherProfileDto.getProfileImageName() != null){
            otherProfileDto.setProfileImageUrl(
                    storageService.getImageByName(otherProfileDto.getProfileImageName())
            );
        }
        if(otherProfileDto.getCoverImageName() != null){
            otherProfileDto.setCoverImageUrl(
                    storageService.getImageByName(otherProfileDto.getCoverImageName())
            );
        }

        /** For CareerHistory **/

        if(!otherProfile.getCareerHistoryList().isEmpty()){
            List<CareerHistoryDto> careerHistoryDtoList = otherProfile.getCareerHistoryList().stream().map(
                    careerHistory -> {
                        CareerHistoryDto careerHistoryDto = modelMapper.map(careerHistory, CareerHistoryDto.class);
                        if(careerHistoryDto.getCompanyLogoName() != null ) {
                            careerHistoryDto.setCompanyLogoUrl(storageService.getImageByName(careerHistoryDto.getCompanyLogoName()));
                        }
                        return careerHistoryDto;
                    }).collect(Collectors.toList());
            otherProfileDto.setCareerHistoryDtoList(careerHistoryDtoList);
        }

        /** Service Provided **/
        if(!otherProfile.getServiceProvidedList().isEmpty()){
            List<ServiceProvidedDto> serviceProvidedDtoList = otherProfile.getServiceProvidedList().stream().map(
                    serviceProvided -> modelMapper.map(serviceProvided, ServiceProvidedDto.class)
            ).collect(Collectors.toList());
            otherProfileDto.setServiceProvidedDtoList(serviceProvidedDtoList);
        }

        if(!otherProfile.getFollowers().isEmpty()){
            otherProfileDto.setFollowerCount(Long.valueOf(otherProfile.getFollowers().size()));
        }

        /** Following **/
        if(!otherProfile.getFollowing().isEmpty()){
            otherProfileDto.setFollowingCount(Long.valueOf(otherProfile.getFollowing().size()));
        }

        /** Neighbors **/
        if(!otherProfile.getNeighbors().isEmpty()){
            otherProfileDto.setNeighborCount(Long.valueOf(otherProfile.getNeighbors().size()));
        }

        Response response = Response.builder()
                .statusCode(HttpStatus.OK.value())
                .message("successfully got another user profile..")
                .otherProfileDto(otherProfileDto)
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
