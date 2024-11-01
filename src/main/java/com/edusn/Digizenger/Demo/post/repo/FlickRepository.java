package com.edusn.Digizenger.Demo.post.repo;

import com.edusn.Digizenger.Demo.auth.entity.User;
import com.edusn.Digizenger.Demo.post.entity.Flick;
import com.edusn.Digizenger.Demo.post.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FlickRepository extends JpaRepository<Flick,Long> {

    Optional<Flick> findByPostAndUser(Post existPost, User user);
}
