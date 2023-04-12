package com.example.demo.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class NotFoundException extends RuntimeException{
    public NotFoundException(Long id){
        super("Could not find the article:: "+id);
    }
    public NotFoundException(String userName){
        super("Could not find the user:: "+userName);
    }
    public NotFoundException(){
        super("Could not find the user:: ");
    }
}
