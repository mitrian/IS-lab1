package com.lab1.exceptions.entity.impl;

import com.lab1.exceptions.entity.EntityAbsenceException;

public class LocationAbsenceException extends EntityAbsenceException {
    public LocationAbsenceException(String message) {
        super(message);
    }

    public LocationAbsenceException(String message, Throwable cause) {
        super(message, cause);
    }
}
