package com.edusn.Digizenger.Demo.post.repo;

import com.edusn.Digizenger.Demo.post.entity.Like;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReplyRepository extends JpaRepository<Like,Long> {
}
