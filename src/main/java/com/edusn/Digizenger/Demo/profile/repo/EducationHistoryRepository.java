package com.edusn.Digizenger.Demo.profile.repo;

import com.edusn.Digizenger.Demo.profile.entity.education_history.EducationHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EducationHistoryRepository extends JpaRepository<EducationHistory,Long> {
}
