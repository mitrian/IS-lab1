package com.lab1.exceptions.entity;

public abstract class EntityUpdateException extends RuntimeException {
    public EntityUpdateException(String message) {
        super(message);
    }

    public EntityUpdateException(String message, Throwable cause) {
        super(message, cause);
    }
}
