package com.edusn.Digizenger.Demo.profile.repo;

import com.edusn.Digizenger.Demo.profile.entity.ServiceProvided;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ServiceProvidedRepository extends JpaRepository<ServiceProvided, Long> {

    @Query("SELECT DISTINCT s FROM ServiceProvided s WHERE s.service LIKE %:service%")
    List<ServiceProvided> findByServiceNameDynamic(String service);
}
