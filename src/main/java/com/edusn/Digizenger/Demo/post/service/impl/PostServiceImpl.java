package com.edusn.Digizenger.Demo.post.service.impl;

import com.edusn.Digizenger.Demo.auth.dto.response.Response;
import com.edusn.Digizenger.Demo.post.dto.PostDto;
import com.edusn.Digizenger.Demo.post.dto.UserDto;
import com.edusn.Digizenger.Demo.auth.entity.User;
import com.edusn.Digizenger.Demo.post.entity.Post;
import com.edusn.Digizenger.Demo.exception.CustomNotFoundException;
import com.edusn.Digizenger.Demo.exception.PostNotFoundException;
import com.edusn.Digizenger.Demo.post.entity.View;
import com.edusn.Digizenger.Demo.post.repo.LikeRepository;
import com.edusn.Digizenger.Demo.post.repo.PostRepository;
import com.edusn.Digizenger.Demo.post.repo.ViewRepository;
import com.edusn.Digizenger.Demo.post.service.PostService;
import com.edusn.Digizenger.Demo.storage.StorageService;
import com.edusn.Digizenger.Demo.utilis.MapperUtil;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

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
        postRepository.save(post);
        PostDto postDto=convertToPostDto(post);
        if(post.getImageName()!=null){
            postDto.setImageName(post.getImageName());
            postDto.setImageUrl(storageService.getImageByName(post.getImageName()));
        }
        postDto.setUserDto(convertToUserDto(user));
        Long likeCount = likeRepository.findByPost(post).stream().count();
        postDto.setLikeCount(likeCount);
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
                .orElseThrow(() -> new PostNotFoundException("Post not found by " + id));
        if (!multipartFile.isEmpty()) {
           String newImageName = storageService.updateImage(multipartFile,imageName);
            post.setImageName(newImageName);
        }

        // Update media if present in request


        post.setUser(user);
        postRepository.save(post);  // Save the updated post

        // Convert to DTO and return response
        PostDto postDto = convertToPostDto(post);

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

    @Override
    public ResponseEntity<Response> getPostByPage(int _page, int _limit,User user) {
        Pageable pageable = PageRequest.of(_page - 1, _limit);
        Page<Post> postPage = postRepository.findAll(pageable);
        if (postPage.isEmpty()) {
            Response response = Response.builder()
                    .message("No more posts available.")
                    .statusCode(HttpStatus.NO_CONTENT.value())
                    .build();
            return new ResponseEntity<>(response, HttpStatus.NO_CONTENT);
        }

        List<PostDto> postDtoList = postPage.getContent().stream().map(post -> {
            UserDto userDto = convertToUserDto(post.getUser());
            Long viewCount = viewRepository.countByPost(post);
            Long likeCount = likeRepository.countByPostAndIsLiked(post,true);
            boolean isLike=post.getLikes().stream().anyMatch(like -> like.getUser().equals(user)&& like.isLiked());
            // Convert post to PostDto and set additional fields
            PostDto postDto = PostServiceImpl.convertToPostDto(post);

            if(post.getUser().getProfile().getProfileImageName()!=null){
                postDto.getProfileDto().setProfileImageName(post.getUser().getProfile().getProfileImageName());
                postDto.getProfileDto().setProfileImageUrl(storageService.getImageByName(post.getUser().getProfile().getProfileImageName()));
            }
            if(post.getImageName() !=null){
                postDto.setImageUrl(storageService.getImageByName(post.getImageName()));

            }
            postDto.setUserDto(userDto);
            postDto.setViewCount(viewCount);
            postDto.setLikeCount(likeCount);
            postDto.setLiked(isLike);
            return postDto;
        }).toList();

        Response response=Response.builder()
                .postDtoList(postDtoList)
                .statusCode(HttpStatus.OK.value())
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





    public static PostDto convertToPostDto(Post post) {
        return MapperUtil.convertToPostDto(post);
    }
    public static UserDto convertToUserDto(User user) {
        return MapperUtil.convertToUserDto(user);
    }




}
