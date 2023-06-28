package com.example.demo.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNAUTHORIZED)
public class AuthException extends RuntimeException{
    public AuthException(String userName, String password){
        super("Username or Password is wrong:::");
    }

    public AuthException(String password){
        super("Current password is not correct:::");
    }
}
