package com.lab1.exceptions.entity;

public abstract class EntityInaccessibleDeleteException extends RuntimeException {
    public EntityInaccessibleDeleteException(String message) {
        super(message);
    }

    public EntityInaccessibleDeleteException(String message, Throwable cause) {
        super(message, cause);
    }
}
