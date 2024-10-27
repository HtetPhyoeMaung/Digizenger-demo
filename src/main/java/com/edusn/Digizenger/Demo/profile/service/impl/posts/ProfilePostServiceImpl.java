package com.edusn.Digizenger.Demo.profile.service.impl.posts;

import com.edusn.Digizenger.Demo.auth.dto.response.Response;
import com.edusn.Digizenger.Demo.auth.entity.User;
import com.edusn.Digizenger.Demo.exception.PostNotFoundException;
import com.edusn.Digizenger.Demo.exception.ProfileNotFoundException;
import com.edusn.Digizenger.Demo.post.dto.PostDto;
import com.edusn.Digizenger.Demo.post.entity.Post;
import com.edusn.Digizenger.Demo.post.repo.LikeRepository;
import com.edusn.Digizenger.Demo.post.repo.PostRepository;
import com.edusn.Digizenger.Demo.post.repo.ViewRepository;
import com.edusn.Digizenger.Demo.post.service.impl.PostServiceImpl;
import com.edusn.Digizenger.Demo.profile.entity.Profile;
import com.edusn.Digizenger.Demo.profile.repo.ProfileRepository;
import com.edusn.Digizenger.Demo.profile.service.ProfilePostService;
import com.edusn.Digizenger.Demo.storage.StorageService;
import com.edusn.Digizenger.Demo.utilis.GetUserByRequest;
import com.edusn.Digizenger.Demo.utilis.MapperUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class ProfilePostServiceImpl implements ProfilePostService {

    private final PostRepository postRepository;
    private final ProfileRepository profileRepository;
    private final GetUserByRequest getUserByRequest;
    private final StorageService storageService;
    private final ViewRepository viewRepository;
    private final LikeRepository likeRepository;

    @Override
    public ResponseEntity<Response> getProfilePosts(HttpServletRequest request,
                                                    int _page,
                                                    int _limit) {
        User user = getUserByRequest.getUser(request);
        Profile profile = profileRepository.findByUser(user);

        List<PostDto> postDtoList = null;
        if(profile.getUser().getPosts() != null){
            Pageable pageable =  PageRequest.of(_page -1, _limit);
            postDtoList = postRepository.findByUserIdOrderByCreatedDateDesc(user.getId(), pageable).stream().map(post -> {
                Long viewCount = viewRepository.countByPost(post);
                Long likeCount = likeRepository.countByPostAndIsLiked(post, true);
                boolean isLike = post.getLikes().stream()
                        .anyMatch(like -> like.getUser().equals(user) && like.isLiked());
                PostDto postDto = MapperUtil.convertToPostDto(post);
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
            }).toList(); // Collect into a List
        }

        if(postDtoList.isEmpty()) throw new PostNotFoundException("post can not found in your profile.");

        Response response = Response.builder()
                .statusCode(HttpStatus.OK.value())
                .message("successfully got profile's posts.")
                .postDtoList(postDtoList)
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Response> getOtherProfilePosts(HttpServletRequest request,
                                                         Long profileId,
                                                         int _page,
                                                         int _limit) {

        User user = getUserByRequest.getUser(request);
        Profile profile = profileRepository.findByUser(user);

        Profile otherProfile = profileRepository.findById(profileId)
                .orElseThrow(() -> new ProfileNotFoundException("profile not exists by id : "+profileId));
        User otherUser = otherProfile.getUser();

        Response response = null;
        if (otherUser.getPosts() != null) {
            Pageable pageable = PageRequest.of(_page -1, _limit);
            Page<Post> postList;

            if(otherProfile.getNeighbors().contains(profile)){
                postList = postRepository.findByUserIdOrderByCreatedDateDesc(otherProfile.getId(), pageable);
            }else if(otherProfile.getFollowers().contains(profile)){
                postList = postRepository.findByUserIdAndPostTypeNotOrderByCreatedDateDesc(otherUser.getId(), Post.PostType.NEIGHBOURS, pageable);
            }else{
                postList = postRepository.findByUserIdAndPostTypeNotAndPostTypeNotOrderByCreatedDateDesc(otherProfile.getId(), Post.PostType.NEIGHBOURS, Post.PostType.FOLLOWERS,pageable);
            }

            List<PostDto> postDtoList = postList.stream().map(post -> {
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
            }).collect(Collectors.toList());// Collect into a List


            response = Response.builder()
                    .statusCode(HttpStatus.OK.value())
                    .message("successfully got other profile's posts.")
                    .postDtoList(postDtoList)
                    .build();

        }

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
