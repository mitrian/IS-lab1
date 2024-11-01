package com.lab1.exceptions.entity.impl;

import com.lab1.exceptions.entity.EntityUpdateException;

public class CoordinatesUpdateException extends EntityUpdateException {
    public CoordinatesUpdateException(String message) {
        super(message);
    }

    public CoordinatesUpdateException(String message, Throwable cause) {
        super(message, cause);
    }
}
