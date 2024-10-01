package com.edusn.Digizenger.Demo.profile.repo;

import com.edusn.Digizenger.Demo.profile.entity.Education;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EducationRepository extends JpaRepository<Education,Long> {
}
