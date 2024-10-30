package com.lab1.exceptions.entity;


public class EntityCreatorException extends RuntimeException{
    public EntityCreatorException(String message){
        super(message);
    }

    public EntityCreatorException(String message, Throwable cause){
        super(message, cause);
    }
}