package com.learnSpringBoot.dreamShops.exceptions;

public class AlreadyExistsException extends RuntimeException{

    public AlreadyExistsException(String message){
        super(message);
    }
}
