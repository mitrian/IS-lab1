package com.lab1.exceptions.entity.impl;

import com.lab1.exceptions.entity.EntityUpdateException;

public class StaffUpdateException extends EntityUpdateException {
    public StaffUpdateException(String message) {
        super(message);
    }

    public StaffUpdateException(String message, Throwable cause) {
        super(message, cause);
    }
}
