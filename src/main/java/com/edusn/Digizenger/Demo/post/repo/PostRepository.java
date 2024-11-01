package com.edusn.Digizenger.Demo.post.repo;

import com.edusn.Digizenger.Demo.post.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post,Long> {
    Page<Post> findByUserIdOrderByCreatedDateDesc(Long id, Pageable pageable);
    Page<Post> findByUserIdAndPostTypeNotOrderByCreatedDateDesc(Long id, Post.PostType postType, Pageable pageable);
    Page<Post> findByUserIdAndPostTypeNotAndPostTypeNotOrderByCreatedDateDesc(Long id, Post.PostType postType1, Post.PostType postType2, Pageable pageable);

    //For other profile 9 image view
    List<Post> findTop9ByUserIdAndPostTypeNotAndImageNameIsNotNull(Long userId, Post.PostType postType);
    List<Post> findTop9ByUserIdAndPostTypeNotAndPostTypeNotAndImageNameIsNotNull(Long userId, Post.PostType postType1, Post.PostType postType2);

    //For other profile get all image
    Page<Post> findByUserIdAndPostTypeNotAndImageNameIsNotNull(Long userId, Post.PostType postType, Pageable pageable);
    Page<Post> findByUserIdAndPostTypeNotAndPostTypeNotAndImageNameIsNotNull(Long userId, Post.PostType postType1, Post.PostType postType2, Pageable pageable);

    //Other profile's ImageCount
    Long countByUserIdAndPostTypeNotAndImageNameIsNotNull(Long userId, Post.PostType postType);
    Long countByUserIdAndPostTypeNotAndPostTypeNotAndImageNameIsNotNull(Long userId, Post.PostType postType1, Post.PostType postType2);



    //For my profile 9 image view
    List<Post> findTop9ByUserIdAndImageNameIsNotNull(Long userId);

    //For my profile get all image;
    Page<Post> findByUserIdAndImageNameIsNotNull(Long userId, Pageable pageable);

    //Count my profile's image
    Long countByUserIdAndImageNameIsNotNull(Long userId);

    Optional<Post> findByPostLinkUrl(String postLinkUrl);
}
