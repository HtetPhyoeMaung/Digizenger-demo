package com.edusn.Digizenger.Demo.auth.repo;

import com.edusn.Digizenger.Demo.auth.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressRepository extends JpaRepository<Address,Long> {
}
