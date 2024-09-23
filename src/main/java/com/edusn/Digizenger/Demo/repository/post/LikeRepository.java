
package com.edusn.Digizenger.Demo.repository.post;

import com.edusn.Digizenger.Demo.entity.post.Comment;
import com.edusn.Digizenger.Demo.entity.post.Like;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeRepository extends JpaRepository<Like,Long> {
}
