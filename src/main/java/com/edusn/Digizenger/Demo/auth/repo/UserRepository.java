package com.edusn.Digizenger.Demo.auth.repo;

import com.edusn.Digizenger.Demo.auth.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByPhone(String phone);
    Long  countByVerifiedTrue();
    Long countByCreatedDateAfter(LocalDateTime last30days);
    Page<User> findByCreatedDateAfter(LocalDateTime last30days, Pageable pageable);
    List<User> findByDateOfBirth(LocalDate today);

}
