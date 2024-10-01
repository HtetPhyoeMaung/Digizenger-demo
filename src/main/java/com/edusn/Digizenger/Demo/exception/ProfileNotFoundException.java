package com.edusn.Digizenger.Demo.exception;

public class ProfileNotFoundException extends RuntimeException{

    public ProfileNotFoundException(String message){
        super(message);
    }
}
