package com.edusn.Digizenger.Demo.repository;

import aj.org.objectweb.asm.commons.Remapper;
import com.edusn.Digizenger.Demo.entity.auth.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByPhone(String phone);


}
