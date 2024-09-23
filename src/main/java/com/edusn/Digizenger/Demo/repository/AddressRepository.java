package com.edusn.Digizenger.Demo.repository;

import com.edusn.Digizenger.Demo.entity.auth.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressRepository extends JpaRepository<Address,Long> {
}
