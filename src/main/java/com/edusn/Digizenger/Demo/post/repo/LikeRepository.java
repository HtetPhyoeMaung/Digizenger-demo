
package com.edusn.Digizenger.Demo.post.repo;

import com.edusn.Digizenger.Demo.post.entity.Like;
import com.edusn.Digizenger.Demo.post.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like,Long> {
    Optional<Like> findByPost(Post post);

    Long countByPost(Post post);
}
