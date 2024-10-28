package com.edusn.Digizenger.Demo.profile.service.impl.images;

import com.edusn.Digizenger.Demo.auth.dto.response.Response;
import com.edusn.Digizenger.Demo.auth.entity.User;
import com.edusn.Digizenger.Demo.exception.ProfileNotFoundException;
import com.edusn.Digizenger.Demo.post.entity.Post;
import com.edusn.Digizenger.Demo.post.repo.PostRepository;
import com.edusn.Digizenger.Demo.profile.dto.response.myProfile.ImageDto;
import com.edusn.Digizenger.Demo.profile.entity.Profile;
import com.edusn.Digizenger.Demo.profile.repo.ProfileRepository;
import com.edusn.Digizenger.Demo.profile.service.ImagesService;
import com.edusn.Digizenger.Demo.storage.StorageService;
import com.edusn.Digizenger.Demo.utilis.GetUserByRequest;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;


@Service
@RequiredArgsConstructor
public class ImagesServiceImpl implements ImagesService{

    private final ProfileRepository profileRepository;
    private final StorageService storageService;
    private final PostRepository postRepository;
    private final GetUserByRequest getUserByRequest;

    /* My Profile Images **/

    @Override
    public ResponseEntity<Response> getProfileImages(HttpServletRequest request) {
        User user = getUserByRequest.getUser(request);
        List<Post> postList = postRepository.findTop9ByUserIdAndImageNameIsNotNull(user.getId());
        List<String> postImageUrlList = new LinkedList<>();

        postList.forEach(post -> {
            String imageUrl = storageService.getImageByName(post.getImageName());
            postImageUrlList.add(imageUrl);
        });

        Long totalImage = postRepository.countByUserIdAndImageNameIsNotNull(user.getId());

        ImageDto imageDto = ImageDto.builder()
                .totalImage(totalImage)
                .imageUrlList(postImageUrlList)
                .build();

        Response response = Response.builder()
                .statusCode(HttpStatus.OK.value())
                .message("successfully got profile's 9 image and total image.")
                .imageDto(imageDto)
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @Override
    public ResponseEntity<Response> getAllImages(HttpServletRequest request, int _page, int _limit) {
        User user = getUserByRequest.getUser(request);
        Pageable pageable = PageRequest.of(_page -1, _limit);

        List<String> imageUrlList = new LinkedList<>();
        Page<Post> postPages = postRepository.findByUserIdAndImageNameIsNotNull(user.getId(), pageable);

        postPages.forEach(post -> {
            String imageUrl = storageService.getImageByName(post.getImageName());
            imageUrlList.add(imageUrl);
        });

        Long totalImage = postRepository.countByUserIdAndImageNameIsNotNull(user.getId());

        ImageDto imageDto = ImageDto.builder()
                .totalImage(totalImage)
                .imageUrlList(imageUrlList)
                .build();

        Response response = Response.builder()
                .statusCode(HttpStatus.OK.value())
                .message("successfully got user's all images and total images.")
                .imageDto(imageDto)
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /* Other Profile Image **/

    @Override
    public ResponseEntity<Response> getOtherProfileImages(HttpServletRequest request, Long profileId) {
        User loggedUser = getUserByRequest.getUser(request);
        Profile loggedProfile = profileRepository.findByUser(loggedUser);

        Profile otherProfile = profileRepository.findById(profileId)
                .orElseThrow(() -> new ProfileNotFoundException("other profile can't found by id: " + profileId));
        User otherUser = otherProfile.getUser();
        Long totalImage;
        List<Post> otherPostList = null;
        if(otherProfile.getNeighbors().contains(loggedProfile)){
            otherPostList = postRepository.findTop9ByUserIdAndImageNameIsNotNull(otherUser.getId());
            totalImage = postRepository.countByUserIdAndImageNameIsNotNull(otherUser.getId());
        }else if(otherProfile.getFollowers().contains(loggedProfile)){
            otherPostList = postRepository.findTop9ByUserIdAndPostTypeNotAndImageNameIsNotNull(otherUser.getId(), Post.PostType.NEIGHBORS);
            totalImage = postRepository.countByUserIdAndPostTypeNotAndImageNameIsNotNull(otherUser.getId(), Post.PostType.NEIGHBORS);
        }else{
            otherPostList = postRepository.findTop9ByUserIdAndPostTypeNotAndPostTypeNotAndImageNameIsNotNull(
                    otherUser.getId(), Post.PostType.NEIGHBORS, Post.PostType.FOLLOWERS
            );
            totalImage = postRepository.countByUserIdAndPostTypeNotAndPostTypeNotAndImageNameIsNotNull(
                    otherUser.getId(), Post.PostType.NEIGHBORS, Post.PostType.FOLLOWERS
            );
        }

        List<String> imageUrlList = new LinkedList<>();

        otherPostList.forEach(post -> {
            String imageUrl = storageService.getImageByName(post.getImageName());
            imageUrlList.add(imageUrl);
        });

        ImageDto imageDto = ImageDto.builder()
                .totalImage(totalImage)
                .imageUrlList(imageUrlList)
                .build();

        Response response = Response.builder()
                .statusCode(HttpStatus.OK.value())
                .message("successfully got other profile's 9 image and total image.")
                .imageDto(imageDto)
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Response> getOtherAllImages(HttpServletRequest request, Long profileId, int _page, int _limit) {
        User loggedUser = getUserByRequest.getUser(request);
        Profile loggedProfile = profileRepository.findByUser(loggedUser);

        Pageable pageable = PageRequest.of(_page -1, _limit);

        Profile otherProfile = profileRepository.findById(profileId)
                .orElseThrow(() -> new ProfileNotFoundException("other profile can't found by id: " + profileId));
        User otherUser = otherProfile.getUser();
        Long totalImage;
        Page<Post> otherPostPages = null;
        if(otherProfile.getNeighbors().contains(loggedProfile)){
            otherPostPages = postRepository.findByUserIdAndImageNameIsNotNull(otherUser.getId(), pageable);
            totalImage = postRepository.countByUserIdAndImageNameIsNotNull(otherUser.getId());
        }else if(otherProfile.getFollowers().contains(loggedProfile)){
            otherPostPages = postRepository.findByUserIdAndPostTypeNotAndImageNameIsNotNull(otherUser.getId(), Post.PostType.NEIGHBORS, pageable);
            totalImage = postRepository.countByUserIdAndPostTypeNotAndImageNameIsNotNull(otherUser.getId(), Post.PostType.NEIGHBORS);
        }else{
            otherPostPages = postRepository.findByUserIdAndPostTypeNotAndPostTypeNotAndImageNameIsNotNull(
                    otherUser.getId(), Post.PostType.NEIGHBORS, Post.PostType.FOLLOWERS, pageable
            );
            totalImage = postRepository.countByUserIdAndPostTypeNotAndPostTypeNotAndImageNameIsNotNull(
                    otherUser.getId(), Post.PostType.NEIGHBORS, Post.PostType.FOLLOWERS
            );
        }

        List<String> imageUrlList = new LinkedList<>();

        otherPostPages.forEach(post -> {
            String imageUrl = storageService.getImageByName(post.getImageName());
            imageUrlList.add(imageUrl);
        });

        ImageDto imageDto = ImageDto.builder()
                .totalImage(totalImage)
                .imageUrlList(imageUrlList)
                .build();

        Response response = Response.builder()
                .statusCode(HttpStatus.OK.value())
                .message("successfully got other profile's  all images and total image.")
                .imageDto(imageDto)
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
