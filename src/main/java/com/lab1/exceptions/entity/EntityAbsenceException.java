package com.lab1.exceptions.entity;

public abstract class EntityAbsenceException extends RuntimeException {
    public EntityAbsenceException(String message) {
        super(message);
    }

    public EntityAbsenceException(String message, Throwable cause) {
        super(message, cause);
    }
}
