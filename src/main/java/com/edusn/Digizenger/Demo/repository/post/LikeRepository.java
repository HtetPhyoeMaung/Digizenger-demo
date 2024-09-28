
package com.edusn.Digizenger.Demo.repository.post;

import com.edusn.Digizenger.Demo.entity.post.Comment;
import com.edusn.Digizenger.Demo.entity.post.Like;
import com.edusn.Digizenger.Demo.entity.post.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like,Long> {
    Optional<Like> findByPost(Post post);
}
