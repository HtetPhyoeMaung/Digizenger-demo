package com.edusn.Digizenger.Demo.profile.repo;

import com.edusn.Digizenger.Demo.profile.entity.career_history.CareerHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CareerHistoryRepository extends JpaRepository<CareerHistory,Long> {
}
