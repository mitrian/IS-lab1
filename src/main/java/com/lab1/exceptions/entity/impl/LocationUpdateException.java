package com.lab1.exceptions.entity.impl;

import com.lab1.exceptions.entity.EntityUpdateException;

public class LocationUpdateException extends EntityUpdateException {
    public LocationUpdateException(String message) {
        super(message);
    }

    public LocationUpdateException(String message, Throwable cause) {
        super(message, cause);
    }
}
