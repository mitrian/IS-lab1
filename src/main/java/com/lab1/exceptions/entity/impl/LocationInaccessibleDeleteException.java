package com.lab1.exceptions.entity.impl;

import com.lab1.exceptions.entity.EntityInaccessibleDeleteException;

public class LocationInaccessibleDeleteException extends EntityInaccessibleDeleteException {
    public LocationInaccessibleDeleteException(String message) {
        super(message);
    }

    public LocationInaccessibleDeleteException(String message, Throwable cause) {
        super(message, cause);
    }
}
