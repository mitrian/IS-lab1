package com.lab1.exceptions.entity.impl;

import com.lab1.exceptions.entity.EntityAbsenceException;

public class OrganizationAbsenceException extends EntityAbsenceException {
    public OrganizationAbsenceException(String message) {
        super(message);
    }

    public OrganizationAbsenceException(String message, Throwable cause) {
        super(message, cause);
    }
}
