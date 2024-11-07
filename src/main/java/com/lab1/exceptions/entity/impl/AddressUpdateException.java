package com.lab1.exceptions.entity.impl;

import com.lab1.exceptions.entity.EntityUpdateException;

public class AddressUpdateException extends EntityUpdateException {
    public AddressUpdateException(String message) {
        super(message);
    }

    public AddressUpdateException(String message, Throwable cause) {
        super(message, cause);
    }
}
