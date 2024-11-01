package com.lab1.exceptions.entity.impl;

import com.lab1.exceptions.entity.EntityAbsenceException;

public class CoordinatesAbsenceException extends EntityAbsenceException {
    public CoordinatesAbsenceException(String message) {
        super(message);
    }

    public CoordinatesAbsenceException(String message, Throwable cause) {
        super(message, cause);
    }
}
