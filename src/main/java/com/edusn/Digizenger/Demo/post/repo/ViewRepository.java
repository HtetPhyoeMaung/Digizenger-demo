package com.edusn.Digizenger.Demo.post.repo;

import com.edusn.Digizenger.Demo.auth.entity.User;
import com.edusn.Digizenger.Demo.post.entity.Post;
import com.edusn.Digizenger.Demo.post.entity.View;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ViewRepository extends JpaRepository<View,Long> {
    Optional<View> findByPostAndUser(Post post, User user);
    Long countByPost(Post post);

}