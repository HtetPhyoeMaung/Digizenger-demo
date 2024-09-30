package com.edusn.Digizenger.Demo.utilis;

import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class UUIDUtil {

    public static String generateUUID() {
        return UUID.randomUUID().toString();
    }

}
