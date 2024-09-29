package com.edusn.Digizenger.Demo.auth.service.impl;

import com.edusn.Digizenger.Demo.auth.repo.AddressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AddressServiceImpl {

    @Autowired
    private AddressRepository addressRepository;
}
