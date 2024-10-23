package com.edusn.Digizenger.Demo.notification.repo;

import com.edusn.Digizenger.Demo.auth.entity.User;
import com.edusn.Digizenger.Demo.notification.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification,Long> {
    Page<Notification> findByUser(User user, Pageable pageable);

    List<Notification> findByUserAndIsReadFalse(User user);
}
