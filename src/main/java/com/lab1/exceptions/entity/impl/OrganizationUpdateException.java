package com.lab1.exceptions.entity.impl;

import com.lab1.exceptions.entity.EntityUpdateException;

public class OrganizationUpdateException extends EntityUpdateException {
    public OrganizationUpdateException(String message) {
        super(message);
    }

    public OrganizationUpdateException(String message, Throwable cause) {
        super(message, cause);
    }
}
