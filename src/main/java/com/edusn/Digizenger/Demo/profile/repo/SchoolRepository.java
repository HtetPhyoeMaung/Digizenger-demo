package com.edusn.Digizenger.Demo.profile.repo;

import com.edusn.Digizenger.Demo.profile.entity.School;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SchoolRepository extends JpaRepository<School,Long> {

    @Query("SELECT DISTINCT s FROM School s WHERE s.schoolName LIKE %:name%")
    List<School> findSchoolByDynamicName(String name);

    boolean existsBySchoolName(String name);

    School findBySchoolName(String name);
}
