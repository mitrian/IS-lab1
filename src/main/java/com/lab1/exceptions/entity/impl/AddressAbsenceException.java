package com.lab1.exceptions.entity.impl;

import com.lab1.exceptions.entity.EntityAbsenceException;

public class AddressAbsenceException extends EntityAbsenceException {
    public AddressAbsenceException(String message) {
        super(message);
    }

    public AddressAbsenceException(String message, Throwable cause) {
        super(message, cause);
    }
}
