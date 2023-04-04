package com.example.demo.Exception;

public class UserNotFoundException extends RuntimeException{
    public UserNotFoundException(Long id){
        super("Could not found the user:: "+id);
    }
    public UserNotFoundException(String email){
        super("Could not found the user:: "+email);
    }
}
