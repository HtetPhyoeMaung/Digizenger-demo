package com.edusn.Digizenger.Demo.repository.post;

import com.edusn.Digizenger.Demo.entity.post.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface PostRepository extends JpaRepository<Post,Long> {
}
