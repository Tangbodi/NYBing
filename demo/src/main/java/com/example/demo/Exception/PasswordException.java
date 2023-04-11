package com.example.demo.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class PasswordException extends RuntimeException{
    public PasswordException(String password){
        super("Password should be minimum eight characters, at least one uppercase letter, one lowercase letter, one number and one special character");
    }
}
