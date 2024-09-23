package com.edusn.Digizenger.Demo.repository.post;

import com.edusn.Digizenger.Demo.entity.post.Comment;
import com.edusn.Digizenger.Demo.entity.post.Media;
import com.edusn.Digizenger.Demo.entity.post.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface MediaRepository extends JpaRepository<Media,Long> {
    List<Media> findAllByPost(Post post);
}
