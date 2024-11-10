package com.lab1.exceptions.entity.impl;

import com.lab1.exceptions.entity.EntityAbsenceException;

public class StaffAbsenceException extends EntityAbsenceException {
    public StaffAbsenceException(String message) {
        super(message);
    }

    public StaffAbsenceException(String message, Throwable cause) {
        super(message, cause);
    }
}
