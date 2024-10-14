package com.edusn.Digizenger.Demo.post.repo;

import com.edusn.Digizenger.Demo.post.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post,Long> {
    List<Post> findTop9ByUserIdOrderByCreatedDateByIdDesc(Long id);
}
