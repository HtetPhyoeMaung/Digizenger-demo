package com.edusn.Digizenger.Demo.service.post.impl;

import com.edusn.Digizenger.Demo.dto.response.Response;
import com.edusn.Digizenger.Demo.dto.response.home.post.MediaDto;
import com.edusn.Digizenger.Demo.dto.response.home.post.PostDto;
import com.edusn.Digizenger.Demo.dto.response.home.UserDto;
import com.edusn.Digizenger.Demo.entity.auth.User;
import com.edusn.Digizenger.Demo.entity.post.Media;
import com.edusn.Digizenger.Demo.entity.post.Post;
import com.edusn.Digizenger.Demo.exception.CustomNotFoundException;
import com.edusn.Digizenger.Demo.exception.PostNotFoundException;
import com.edusn.Digizenger.Demo.repository.post.PostRepository;
import com.edusn.Digizenger.Demo.service.post.MediaService;
import com.edusn.Digizenger.Demo.service.post.PostService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostServiceImpl implements PostService {
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private MediaService mediaService;
    @Override
    public ResponseEntity<PostDto> upload(Post requestPost, User user) {
        Post post=Post.builder()
                .content(requestPost.getContent())
                .isPublic(requestPost.getIsPublic())
                .createdDate(LocalDateTime.now())
                .user(user)
                .build();

        if(!requestPost.getMedia().isEmpty()){
            List<Media> mediaList = new LinkedList<>();
            for(Media media:requestPost.getMedia()){
                media.setPost(post);
                if(media.getMediaType().equals(Media.MediaType.IMAGE)){
                    String imgUrl=media.getMediaUrl();
                }else if(media.getMediaType().equals(Media.MediaType.VIDEO)){
                    String videoUrl=media.getMediaUrl();
                }

                mediaList.add(media);
            }
            post.setMedia(mediaList);
        }
        postRepository.save(post);
        PostDto postDto=convertToPostDto(post);
        List<MediaDto> mediaDtos = post.getMedia().stream()
                .map(media -> modelMapper.map(media, MediaDto.class))
                .collect(Collectors.toList());
        postDto.setMediaDtos(mediaDtos);
        postDto.setUserDto(modelMapper.map(user, UserDto.class));
        return new ResponseEntity<>(postDto, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<PostDto> updatePost(Post requestPost, User user) {
        // Find and update the post
        var post = postRepository.findById(requestPost.getId())
                .map(existPost -> {
                    existPost.setContent(requestPost.getContent());
                    existPost.setModifiedDate(LocalDateTime.now());
                    existPost.setIsPublic(requestPost.getIsPublic());
                    return existPost;
                })
                .orElseThrow(() -> new PostNotFoundException("Post not found by " + requestPost.getId()));

        // Update media if present in request
        if (!requestPost.getMedia().isEmpty()) {
            // Handle both existing and new media in requestPost
            List<Media> updatedMediaList = mediaService.updateMedia(post, requestPost.getMedia());
            post.setMedia(updatedMediaList);  // Set updated media list to post
        }

        post.setUser(user);
        postRepository.save(post);  // Save the updated post

        // Convert to DTO and return response
        PostDto postDto = convertToPostDto(post);
        List<MediaDto> mediaDtos = post.getMedia().stream()
                .map(media -> modelMapper.map(media, MediaDto.class))
                .collect(Collectors.toList());
        postDto.setMediaDtos(mediaDtos);
        postDto.setUserDto(modelMapper.map(user, UserDto.class));
        return new ResponseEntity<>(postDto, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> deletePost(long id) {
        Post post=postRepository.findById(id).orElseThrow(()->new CustomNotFoundException("Post not found by this "+id));
        postRepository.delete(post);
        return  new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    /*, Sort.by("createdDate").descending()*/

    @Override
    public ResponseEntity<Response> getPostByPage(int _page, int _limit) {
        Pageable pageable = PageRequest.of(_page - 1, _limit, Sort.by("createdDate").descending());

        // Fetch paginated posts
        Page<Post> postPage = postRepository.findAll(pageable);

        List<PostDto> postDto= postPage.getContent().stream()
                .map(PostServiceImpl::convertToPostDto)
                .toList();
        Response response=Response.builder()
                .postDto(postDto)
                .statusCode(HttpStatus.OK.value())
                .build();
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    public static PostDto convertToPostDto(Post post) {
        PostDto postDto = new PostDto();
        postDto.setId(post.getId());
        postDto.setContent(post.getContent());
        postDto.setCreatedDate(post.getCreatedDate());
        postDto.setModifiedDate(post.getModifiedDate());
        postDto.setIsPublic(post.getIsPublic());

        List<MediaDto> mediaDTOs = post.getMedia().stream()
                .map(PostServiceImpl::convertToMediaDto)
                .collect(Collectors.toList());

        postDto.setMediaDtos(mediaDTOs);
        return postDto;
    }
    private static MediaDto convertToMediaDto(Media media) {
        MediaDto mediaDto = new MediaDto();
        mediaDto.setId(media.getId());
        mediaDto.setMediaUrl(media.getMediaUrl());
        mediaDto.setMediaType(media.getMediaType());
        return mediaDto;
    }



}
