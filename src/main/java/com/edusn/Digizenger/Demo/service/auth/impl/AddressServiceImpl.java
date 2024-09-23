package com.edusn.Digizenger.Demo.service.auth.impl;

import com.edusn.Digizenger.Demo.repository.AddressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AddressServiceImpl {

    @Autowired
    private AddressRepository addressRepository;
}
