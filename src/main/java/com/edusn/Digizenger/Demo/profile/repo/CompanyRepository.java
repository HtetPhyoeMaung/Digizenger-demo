package com.edusn.Digizenger.Demo.profile.repo;

import com.edusn.Digizenger.Demo.profile.entity.career_history.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CompanyRepository extends JpaRepository<Company, Long> {

    @Query("SELECT DISTINCT c FROM Company c WHERE c.companyName LIKE %:name%")
    List<Company> findCompanyByDynamicName(String name);

    boolean existsByCompanyName(String name);

    Company findByCompanyName(String name);
}
