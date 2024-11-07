package com.lab1.exceptions.entity.impl;

import com.lab1.exceptions.entity.EntityInaccessibleDeleteException;

public class AddressInaccessibleDeleteDeleteException extends EntityInaccessibleDeleteException {
    public AddressInaccessibleDeleteDeleteException(String message) {
        super(message);
    }

    public AddressInaccessibleDeleteDeleteException(String message, Throwable cause) {
        super(message, cause);
    }
}
