package com.edusn.Digizenger.Demo.post.service.impl;

import com.edusn.Digizenger.Demo.auth.dto.response.Response;
import com.edusn.Digizenger.Demo.post.dto.PostDto;
import com.edusn.Digizenger.Demo.post.dto.UserDto;
import com.edusn.Digizenger.Demo.auth.entity.User;
import com.edusn.Digizenger.Demo.post.entity.Post;
import com.edusn.Digizenger.Demo.exception.CustomNotFoundException;
import com.edusn.Digizenger.Demo.exception.PostNotFoundException;
import com.edusn.Digizenger.Demo.post.repo.LikeRepository;
import com.edusn.Digizenger.Demo.post.repo.PostRepository;
import com.edusn.Digizenger.Demo.post.service.PostService;
import com.edusn.Digizenger.Demo.storage.StorageService;
import com.edusn.Digizenger.Demo.utilis.UUIDUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

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
                    .viewsCount(0L)
                    .user(user)
                    .build();

        }else {


            post = Post.builder()
                    .description(description)
                    .postType(postType)
                    .viewsCount(0L)
                    .createdDate(LocalDateTime.now())
                    .user(user)
                    .build();
        }


        postRepository.save(post);
        PostDto postDto=convertToPostDto(post);

        postDto.setUserDto(modelMapper.map(user, UserDto.class));
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
        postDto.setUserDto(modelMapper.map(user, UserDto.class));
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
    public ResponseEntity<Response> getPostByPage(int _page, int _limit) {
        Pageable pageable = PageRequest.of(_page - 1, _limit);
        List<User>userList = new LinkedList<>();
        // Fetch paginated posts
        Page<Post> postPage = postRepository.findAll(pageable);

        for(Post post:postPage){
            userList.add(post.getUser());
        }
        List<UserDto>userDtoList=userList.stream()
                .map(user -> new UserDto
                        (user.getFirstName(),
                                user.getLastName(),
                                user.getFollowers()))
                .toList();
        List<PostDto> postDtoList= postPage.getContent().stream()
                .map(PostServiceImpl::convertToPostDto)
                .toList();
        Response response=Response.builder()
                .postDtoList(postDtoList)
                .userDtoList(userDtoList)
                .statusCode(HttpStatus.OK.value())
                .build();
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Response> getImage(String imageName) throws IOException {
      byte[] imageBytes = storageService.getImageByName(imageName);
       Response response = Response.builder()
               .statusCode(HttpStatus.OK.value())
               .imageByte(imageBytes)
               .build();
       return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,"attachment; filename=\""+imageName+"\"").body(response);
    }

    public static PostDto convertToPostDto(Post post) {
        PostDto postDto = new PostDto();
        postDto.setId(post.getId());
        postDto.setDescription(post.getDescription());
        postDto.setCreatedDate(post.getCreatedDate());
        postDto.setModifiedDate(post.getModifiedDate());
        postDto.setPostType(post.getPostType());
        postDto.setImageName(post.getImageName());
        postDto.setViewCount(post.getViewsCount());


        return postDto;
    }




}
