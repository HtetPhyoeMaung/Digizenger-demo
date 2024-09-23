package com.edusn.Digizenger.Demo.exception;


public class LoginNameExistException extends RuntimeException{
    public LoginNameExistException(String message){
        super(message);
    }
}
