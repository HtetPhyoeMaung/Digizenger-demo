package com.edusn.Digizenger.Demo.repository.post;

import com.edusn.Digizenger.Demo.entity.post.Comment;
import com.edusn.Digizenger.Demo.entity.post.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment,Long> {
}
