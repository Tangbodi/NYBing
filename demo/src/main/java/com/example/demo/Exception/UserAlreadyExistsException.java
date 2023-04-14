package com.example.demo.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT)
public class UserAlreadyExistsException extends RuntimeException{
    public UserAlreadyExistsException(){
        super("Username or email is already exists:: ");
    }
}
