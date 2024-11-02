package com.edusn.Digizenger.Demo.post.service.impl;

import com.edusn.Digizenger.Demo.auth.dto.response.Response;
import com.edusn.Digizenger.Demo.notification.dto.NotificationDto;
import com.edusn.Digizenger.Demo.notification.entity.Notification;
import com.edusn.Digizenger.Demo.notification.repo.NotificationRepository;
import com.edusn.Digizenger.Demo.post.dto.PostDto;
import com.edusn.Digizenger.Demo.post.dto.UserDto;
import com.edusn.Digizenger.Demo.auth.entity.User;
import com.edusn.Digizenger.Demo.post.entity.Post;
import com.edusn.Digizenger.Demo.exception.CustomNotFoundException;
import com.edusn.Digizenger.Demo.post.entity.View;
import com.edusn.Digizenger.Demo.post.repo.LikeRepository;
import com.edusn.Digizenger.Demo.post.repo.PostRepository;
import com.edusn.Digizenger.Demo.post.repo.ViewRepository;
import com.edusn.Digizenger.Demo.post.service.PostService;
import com.edusn.Digizenger.Demo.profile.dto.response.myProfile.ProfileDto;
import com.edusn.Digizenger.Demo.profile.dto.response.otherProfile.OtherProfileDto;
import com.edusn.Digizenger.Demo.profile.entity.Profile;
import com.edusn.Digizenger.Demo.profile.entity.RelationshipStatus;
import com.edusn.Digizenger.Demo.profile.repo.ProfileRepository;
import com.edusn.Digizenger.Demo.storage.StorageService;
import com.edusn.Digizenger.Demo.utilis.CommonUtil;
import com.edusn.Digizenger.Demo.utilis.DateUtil;
import com.edusn.Digizenger.Demo.utilis.GeneratePostUrl;
import com.edusn.Digizenger.Demo.utilis.MapperUtil;

import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static com.edusn.Digizenger.Demo.utilis.MapperUtil.convertToUserDto;

@Slf4j
@Service
public  class PostServiceImpl implements PostService {
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private StorageService storageService;
    @Autowired
    private LikeRepository likeRepository;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private ViewRepository viewRepository;
    @Autowired
    private ProfileRepository profileRepository;
    @Autowired
    private GeneratePostUrl generatePostUrl;
    @Autowired
    private CommonUtil commonUtil;
    @Autowired
    private DateUtil dateUtil;
    @Autowired
    private NotificationRepository notificationRepository;
    @Autowired
    private SimpMessagingTemplate messagingTemplate;


    @Override
    public ResponseEntity<Response> upload(String description, Post.PostType postType, User user, MultipartFile multipartFile) throws IOException {
         Post post;
        if (multipartFile!=null) {
            String filename =storageService.uploadImage(multipartFile);
            post = Post.builder()
                    .description(description)
                    .postType(postType)
                    .createdDate(LocalDateTime.now())
                    .imageName(filename)
                    .user(user)
                    .build();
        }else {
            post = Post.builder()
                    .description(description)
                    .postType(postType)
                    .createdDate(LocalDateTime.now())
                    .user(user)
                    .build();
        }
        String postLinkUrl = generatePostUrl.generateSecurePostLinkUrl((int)(Math.random() * 9000) + 1000);

        post.setPostLinkUrl(postLinkUrl);
        postRepository.save(post);

        Profile profile = post.getUser().getProfile();
        ProfileDto profileDto = ProfileDto.builder()
                .id(profile.getId())
                .username(profile.getUsername())
                .followerCount((long) profile.getFollowers().size())
                .build();
        if(post.getUser().getProfile().getProfileImageName()!=null){
          profileDto.setProfileImageName(post.getUser().getProfile().getProfileImageName());
           profileDto.setProfileImageUrl(storageService.getImageByName(post.getUser().getProfile().getProfileImageName()));
        }else {
            profileDto.setProfileImageUrl("");
        }
        PostDto postDto = MapperUtil.convertToPostDto(post);
        postDto.setUserDto(convertToUserDto(user));
        postDto.setProfileDto(profileDto);

        Response response = Response.builder()
                .statusCode(HttpStatus.CREATED.value())
                .message("Post created successfully")
                .postDto(postDto)
                .build();
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }


    @Override
    public ResponseEntity<Response> updatePost(Long id,String description, Post.PostType postType,User user,MultipartFile multipartFile,String imageName) throws IOException {
        Post  post = postRepository.findById(id)
                .map(existPost -> {
                    existPost.setDescription(description);
                    existPost.setModifiedDate(LocalDateTime.now());
                    existPost.setPostType(postType);
                    return existPost;
                })
                .orElseThrow(() -> new CustomNotFoundException("Post not found by " + id));
        if (!multipartFile.isEmpty()) {
           String newImageName = storageService.updateImage(multipartFile,imageName);
            post.setImageName(newImageName);
        }

        // Update media if present in request


        post.setUser(user);
        postRepository.save(post);  // Save the updated post

        // Convert to DTO and return response
        PostDto postDto = MapperUtil.convertToPostDto(post);
        if (post.getImageName()!=null) {
            postDto.setImageName(post.getImageName());
            postDto.setImageUrl(storageService.getImageByName(post.getImageName()));
        }


        postDto.setUserDto(convertToUserDto(user));
        Response response = Response.builder()
                .statusCode(HttpStatus.OK.value())
                .message("Post updated successfully")
                .postDto(postDto)
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> deletePost(long id) {
        Post post=postRepository.findById(id).orElseThrow(()->new CustomNotFoundException("Post not found by this "+id));
        postRepository.delete(post);
        return  new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }






    public ResponseEntity<Response> getNewFeeds(int _page, int _limit, User user){

        Profile profile = profileRepository.findByUser(user);
        Pageable pageable = PageRequest.of(_page -1,_limit, Sort.by(Sort.Direction.DESC, "createdDate"));
        Page<Post> postPage = postRepository.findAll(pageable);

        if (postPage.isEmpty()) {
            Response response = Response.builder()
                    .message("No more posts available.")
                    .statusCode(HttpStatus.NO_CONTENT.value())
                    .build();
            return new ResponseEntity<>(response, HttpStatus.NO_CONTENT);
        }


        List<PostDto> postDtoList = new LinkedList<>();

        postPage.forEach(post -> {
                    PostDto postDto = new PostDto();
                    if (post.getUser().getProfile().getNeighbors().contains(profile)) {
                        postDto = commonForEachPost(post, user, profile);
                        postDtoList.add(postDto);
                    } else if (post.getUser().getProfile().getFollowers().contains(profile)) {
                        if (post.getPostType() != Post.PostType.NEIGHBORS) {
                            postDto = commonForEachPost(post, user, profile);
                            postDtoList.add(postDto);
                        }
                    } else {
                        if (post.getPostType() == Post.PostType.EVERYONE) {
                            postDto = commonForEachPost(post, user, profile);
                            postDtoList.add(postDto);
                        }
                    }
                    List<ProfileDto> flickUserDtoList = commonUtil.getFlickDtoListFromPost(post);


                    postDto.setFlickUserDtoList(flickUserDtoList);
                    postDto.setFlickAmount(flickUserDtoList.size());
                    Notification notificationforNeighbor = Notification.builder()
                            .createDate(LocalDateTime.now())
                            .isRead(false)
                            .user(post.getUser())
                            .post(post)
                            .profile(user.getProfile())
                            .message(user.getProfile().getUsername() + " was flicked " + "\nPost : " + post.getPostLinkUrl())
                            .type(Notification.Type.FLICK)
                            .build();

                    NotificationDto notificationDtoForNeighbors = NotificationDto.builder()
                            .message(user.getProfile().getUsername() + " was flicked " + "\nPost : " + post.getPostLinkUrl())
                            .isRead(notificationforNeighbor.isRead())
                            .type(Notification.Type.FLICK)
                            .createDate(dateUtil.formattedDate(notificationforNeighbor.getCreateDate()))
                            .build();


                    user.getProfile().getNeighbors().forEach(neighbors -> {
                        notificationforNeighbor.setUser(neighbors.getUser());
                        notificationRepository.save(notificationforNeighbor);
                        notificationDtoForNeighbors.setId(notificationforNeighbor.getId());
                        messagingTemplate.convertAndSendToUser(String.valueOf(neighbors.getId()), "/queue/private-notification", notificationDtoForNeighbors);

                    });
                });


            Response response = Response.builder()
                    .statusCode(HttpStatus.OK.value())
                    .message("successfully got new feed")
                    .postDtoList(postDtoList)
                    .build();
            return new ResponseEntity<>(response, HttpStatus.OK);


    }

    @Override
    public ResponseEntity<Response> findByPostLinkUrl(String postLinkUrl) {
      Post post =  postRepository.findByPostLinkUrl(postLinkUrl).orElseThrow(
              ()-> new CustomNotFoundException("Post not found by "+postLinkUrl)
      );
      PostDto postDto = MapperUtil.convertToPostDto(post);
      Profile profile = post.getUser().getProfile();
      ProfileDto profileDto = ProfileDto.builder()
                      .id(profile.getId())
              .username(profile.getUsername())
              .profileImageUrl(profile.getProfileImageName()!=null?storageService.getImageByName(profile.getProfileImageName()):"")
                              .build();
      postDto.setProfileDto(profileDto);
      postDto.setPostLinkUrl(post.getPostLinkUrl());
      postDto.setLikeCount(likeRepository.countByPostAndIsLiked(post,true));
      postDto.setViewCount(viewRepository.countByPost(post));
      List<ProfileDto> flickUserDtoList = commonUtil.getFlickDtoListFromPost(post);
      postDto.setFlickUserDtoList(flickUserDtoList);
      postDto.setFlickAmount(post.getFlicks().size());
      Response response = Response.builder()
              .statusCode(HttpStatus.OK.value())
              .message("Success")
              .postDto(postDto)
              .build();
      return new ResponseEntity<>(response,HttpStatus.OK);
    }


    @Override
    public ResponseEntity<Response> increaseView(Long id,User user) {
     Post  post = postRepository.findById(id).orElseThrow(()->new CustomNotFoundException("Post not found by"+id));
     Optional<View> alreadyView=viewRepository.findByPostAndUser(post,user);
        Response response;
     if(alreadyView.isPresent()){
          response=Response.builder()
                 .statusCode(HttpStatus.OK.value())
                 .message("User Already View Post"+post.getId())
                 .build();
     }else{
         viewRepository.save(View.builder()
                 .post(post)
                 .user(user)
                 .build());
         response=Response.builder()
                 .statusCode(HttpStatus.OK.value())
                 .message("Increase View Count  Post"+post.getId())
                 .build();
     }
        return new ResponseEntity<>(response,HttpStatus.OK);
    }





    private PostDto commonForEachPost(Post post, User user , Profile loggedProfile){
        UserDto userDto = MapperUtil.convertToUserDto(post.getUser());
        Long viewCount = viewRepository.countByPost(post);
        Long likeCount = likeRepository.countByPostAndIsLiked(post,true);
        boolean isLike=post.getLikes().stream().anyMatch(like -> like.getUser().equals(user)&& like.isLiked());
        // Convert post to PostDto and set additional fields
        PostDto postDto = MapperUtil.convertToPostDto(post);
        Profile postOwnerProfile = post.getUser().getProfile();
        OtherProfileDto postOwnerProfileDto = OtherProfileDto.builder()
                .id(postOwnerProfile.getId())
                .username(postOwnerProfile.getUsername())
                .profileImageUrl(postOwnerProfile.getProfileImageName())
                .followerCount((long) postOwnerProfile.getFollowers().size())
                .build();

        if(postOwnerProfile.getNeighbors().contains(loggedProfile)){
            postOwnerProfileDto.setRelationshipStatus(RelationshipStatus.NEIGHBOURS);
        } else if (postOwnerProfile.getFollowers().contains(loggedProfile)) {
            postOwnerProfileDto.setRelationshipStatus(RelationshipStatus.FOLLOWING);
        } else if (postOwnerProfile.getFollowing().contains(loggedProfile)) {
            postOwnerProfileDto.setRelationshipStatus(RelationshipStatus.FOLLOW_BACK);
        } else {
            postOwnerProfileDto.setRelationshipStatus(RelationshipStatus.FOLLOW);
        }

        if(post.getUser().getProfile().getProfileImageName()!=null){
            postOwnerProfileDto.setProfileImageName(post.getUser().getProfile().getProfileImageName());
            postOwnerProfileDto.setProfileImageUrl(storageService.getImageByName(post.getUser().getProfile().getProfileImageName()));
        }else {
            postOwnerProfileDto.setProfileImageUrl("");
        }
        if(post.getImageName() !=null){
            postDto.setImageUrl(storageService.getImageByName(post.getImageName()));

        }else {
            postDto.setImageUrl("");
        }

        postDto.setOtherProfileDto(postOwnerProfileDto);
        postDto.setUserDto(userDto);
        postDto.setViewCount(viewCount);
        postDto.setLikeCount(likeCount);
        postDto.setLiked(isLike);
        return postDto;
    }

}



