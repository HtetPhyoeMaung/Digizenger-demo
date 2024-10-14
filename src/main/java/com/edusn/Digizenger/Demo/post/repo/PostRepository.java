package com.edusn.Digizenger.Demo.post.repo;

import com.edusn.Digizenger.Demo.post.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post,Long> {
    Page<Post> findByUserIdOrderByCreatedDateDesc(Long id, Pageable pageable);
    Page<Post> findByUserIdAndPostTypeNotOrderByCreatedDateDesc(Long id, Post.PostType postType, Pageable pageable);
    Page<Post> findByUserIdAndPostTypeNotAndPostTypeNotOrderByCreatedDateDesc(Long id, Post.PostType postType1, Post.PostType postType2, Pageable pageable);
    List<Post> findTop9ByUserIdOrderByCreatedDateDesc(Long id, Pageable pageable);
}
