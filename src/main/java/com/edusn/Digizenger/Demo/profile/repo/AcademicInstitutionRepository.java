package com.edusn.Digizenger.Demo.profile.repo;

import com.edusn.Digizenger.Demo.profile.entity.AcademicInstitution;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AcademicInstitutionRepository extends JpaRepository<AcademicInstitution,Long> {

    @Query("SELECT DISTINCT a FROM AcademicInstitution a WHERE a.name LIKE %:name%")
    List<AcademicInstitution> academicInstitutionList(String name);
}
