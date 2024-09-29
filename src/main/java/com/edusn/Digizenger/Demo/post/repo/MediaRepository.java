package com.edusn.Digizenger.Demo.post.repo;

import com.edusn.Digizenger.Demo.post.entity.Media;
import com.edusn.Digizenger.Demo.post.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MediaRepository extends JpaRepository<Media,Long> {
    List<Media> findAllByPost(Post post);
}
